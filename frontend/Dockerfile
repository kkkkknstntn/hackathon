# Установка зависимости
FROM node:20-alpine AS build

# Установка рабочей директории
WORKDIR /app

# Копируем package.json и устанавливаем зависимости
COPY package*.json ./
RUN npm install

# Копируем остальные файлы и билдим проект
COPY . .
RUN npm run build

# Production image с nginx
FROM nginx:alpine

# Копируем билд из предыдущего этапа
COPY --from=build /app/dist /usr/share/nginx/html

# Копируем nginx конфиг (опционально)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Порт по умолчанию
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
