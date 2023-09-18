FROM node:18.17-alpine as build

LABEL maintainer="miniyus"

RUN mkdir /react

WORKDIR /react

COPY . .

RUN yarn install
RUN yarn run build

FROM nginx:stable-alpine AS base

LABEL maintainer="miniyu97@gmail.com"

RUN apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone \
    apk del tzdata

RUN mkdir -p /var/www/react

FROM base AS deploy

WORKDIR /var/www/react

COPY --from=build /react/build ./build
COPY nginx/nginx.conf /etc/nginx/nginx.conf
COPY nginx/default.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000