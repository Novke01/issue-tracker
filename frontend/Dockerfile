FROM node:9.3.0-alpine

COPY . /frontend
WORKDIR "/frontend"
RUN npm install -g gyp && npm install -g @angular/cli && npm install

CMD ["ng", "serve", "--host", "0.0.0.0"]

EXPOSE 4200