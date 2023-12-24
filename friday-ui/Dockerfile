FROM node:20.10-alpine AS build

LABEL maintainer="miniyus"

RUN mkdir /react

WORKDIR /react

COPY . .

RUN yarn install
RUN yarn run build

FROM node:18.17-alpine AS base

LABEL maintainer="miniyu97@gmail.com"

RUN apk --no-cache add tzdata && \
    cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone \
    apk del tzdata

RUN npm install -g serve

FROM base AS deploy

WORKDIR /react

EXPOSE 3000

ENTRYPOINT ["serve", "-s", "."]
