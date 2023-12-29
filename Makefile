PRJ_NAME=Friday
PRJ_DESC=$(PRJ_NAME) Deployments Makefile
PRJ_BASE=$(shell pwd)


compose=""
env=""
email="miniyu97@gmail.com"

.DEFAULT: help
.SILENT:;

##help: helps (default)
.PHONY: help
help: Makefile
	echo ""
	echo " $(PRJ_DESC)"
	echo ""
	echo " Usage:"
	echo ""
	echo "	make {command}"
	echo ""
	echo " Commands:"
	echo ""
	sed -n 's/^##/	/p' $< | column -t -s ':' |  sed -e 's/^/ /'
	echo ""

##pull branch={git branch name} env={local,prod,dev,stage}: git pull from github repository
.PHONY: pull
pull:
	./scripts/git.sh $(branch) $(env)
	echo "make pull: Complete pull git repository."

##secret reveal env={local,prod,dev,stage}: git secret reveal
.PHONY: secret-reveal
secret-reveal: 
	echo "[Secret: reveal] $(env)"
ifeq ($(env), "") 
	echo "make secret-reveal: env option is empty."
else
	git secret reveal -f .env.$(env)
	echo "make secret-reveal: Complete git secret reveal."
endif

##start env={local,prod,dev,stage} compose={dev,cert,''}: docker compose up
.PHONY: start
start:
	./scripts/docker-compose.sh $(compose) $(env)
	./scripts/docker-clean.sh
	echo "make start: Complete docker compose up."

##build env={local,prod,dev,stage} compose={dev,cert,''}: docker compose build and start up
.PHONY: build
build: secret-reveal
	./scripts/docker-compose.sh $(compose) $(env)
	./scripts/docker-clean.sh
	echo "make build: Complete docker compose build and up."

##ci.setenv: set env variables on ci
.PHONY: ci.setenv
ci.setenv:
	./scripts/circleci-setenv.sh

##ci.build env={local,prod,dev,stage} mod={backend,frontend}: build on ci
.PHONY: ci.build
ci.build: env:=$(env)
ci.build: mod:=$(mod)
ci.build:
ifeq ($(env),dev)
	@echo "env is dev, no tests"
	./scripts/docker-build.sh $(env) $(mod)
else
	./scripts/docker-build.sh $(env) $(mod) "--withTest"
endif
	./scripts/docker-save.sh $(mod)
	echo "make ci.build: Complete docker image build on ci."

##ci.deploy env={local,prod,dev,stage}: docker compose up on ci
.PHONY: ci.deploy
ci.deploy: env:=$(env)
ci.deploy: secret-reveal
	./scripts/docker-compose.sh $(compose) $(env)
	./scripts/docker-clean.sh
	echo "make ci.deploy: Complete docker compose up on ci."


##cert: create or start certbot docker container 
.PHONY: cert
cert: compose:=cert
cert: env:=$(env)
cert: build
	docker logs certbot-cert
	docker rm certbot-cert
	echo "make cert: Complete start certbot docker container"

##cert.certonly env={prod,dev,stage} domain={your-domain} email={your-email}: run certbot, certificate only
.PHONY: cert.certonly
cert.certonly: domain:=$(domain) email:=$(email)
cert.certonly:
	echo "[Certonly]"
	echo "option: doamin=$(domain)"
	echo "option: email=$(email)"
	docker compose -f docker-compose.cert.yml run certbot certonly --webroot --webroot-path /var/www/certbot --agree-tos --email $(email) -d $(domain)
	echo "make certonly: Complete run certbot docker container with certonly"

##cert.renew env={prod,dev,stage}: renew SSL certificate
.PHONY: cert.renew
cert.renew: env:=$(env)
cert.renew: 
	echo "[Cert: renew]: $(env)"
	docker compose -f docker-compose.cert.yml --env-file .env.$(env) run certbot renew
	echo "make cert.renew: Complete renew SSL certificate"

##cert.certificates env={prod,dev,stage}: check SSL certificates
.PHONY: cert.certificates
cert.certificates: env:=$(env)
cert.certificates:
	echo "[Cert: certificates]: $(env)"
	docker compose -f docker-compose.cert.yml --env-file .env.$(env) run certbot certificates
	echo "make cert.certificates: Complete check SSL certificates"

# register crontab
define register_crontab
	echo "$(1)"
	crontab -l | { cat; echo "$(1)"; } | crontab -
endef

# remove crontab
define remove_crontab
	crontab -l | grep -v "$(1)" | crontab -
endef


##crontab.renew: register renew command in crontable
.PHONY: crontab.renew
crontab.renew: env:=$(env)
crontab.renew: cycle:="0 3 1 * *" 
crontab.renew: cmd:="cd $(CURDIR) && make cert.renew env=$(env)"
crontab.renew: 
	echo "[Crontab: renew]"
	$(call remove_crontab, "$(cmd)")
	$(call register_crontab,"$(cycle) $(cmd)")
	echo "crontab -l"
	crontab -l
	echo "make crontab.renew: Complete register renew command in crontable"

##db.standalone: create mysql & redis container for dev, stage
.PHONY: db.standalone
db.standalone:
	git secret reveal -f ./development/.mysql.env
	docker compose -f ./development/docker-compose.db.yml --env-file ./development/.mysql.env up -d --build
	echo "make mysql: Complete create mysql container"
