FROM node:9.3.0-alpine as builder

COPY package.json package-lock.json ./

RUN npm set progress=false && npm config set depth 0 && npm cache clean --force

RUN npm install && mkdir frontend && cp -R ./node_modules ./frontend

WORKDIR /frontend

COPY . .

RUN $(npm bin)/ng build --prod --build-optimizer


FROM kyma/docker-nginx

COPY --from=builder /frontend/dist /var/www