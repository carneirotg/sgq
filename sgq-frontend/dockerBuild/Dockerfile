FROM nginx:1.17-alpine

COPY build/ /var/www/sgq/html/
COPY sgq_site.conf /etc/nginx/conf.d/

RUN rm /etc/nginx/conf.d/default.conf

EXPOSE 80