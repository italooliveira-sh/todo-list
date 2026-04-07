# ESTÁGIO 1: Build (Compilação com Node.js)
FROM node:20-alpine AS build
WORKDIR /app

# 1. Copiar arquivos de dependências primeiro
COPY package.json package-lock.json ./
RUN npm install

# 2. Copiar todo o código e gerar o build de produção
COPY . .
RUN npm run build -- --configuration production

# ESTÁGIO 2: Execução (Servidor Web com Nginx)
FROM nginx:alpine

# 3. Copiar os arquivos gerados no estágio anterior para a pasta do Nginx
# Ajustado conforme seu angular.json: dist/todo-pro-web/browser
COPY --from=build /app/dist/todo-pro-web/browser /usr/share/nginx/html

# 4. Copiar nossa configuração customizada
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expõe a porta 80 do container
EXPOSE 80

# Inicia o Nginx em modo foreground
CMD ["nginx", "-g", "daemon off;"]
