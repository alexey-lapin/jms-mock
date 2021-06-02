#!/bin/sh
set -e
: "${UI_PUBLIC_PATH:=}"
: "${UI_API_BASE_PATH:=$UI_PUBLIC_PATH/api}"

sed -i "s@<PUBLIC_PATH_REPLACE>@$UI_PUBLIC_PATH@g" /usr/share/nginx/html/index.html
sed -i "s@<PUBLIC_PATH_REPLACE>@$UI_PUBLIC_PATH@g" /usr/share/nginx/html/js/app.*.js
sed -i "s@<API_BASE_PATH_REPLACE>@$UI_API_BASE_PATH@g" /usr/share/nginx/html/js/app.*.js
