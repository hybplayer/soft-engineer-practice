# soft-engineer practice

## 1. front-end

`cd frontend/travel-assistant & npm install & npm run serve`

## 2. back-end

`git submodule update --init --recursive & cd backend & ./gradlew build & ./gradlew run`

## 3. database

`docker-compose -f 'backend/database/docker-compose.yaml' up -d`

## 4. Annotation

本地运行修改一些 URL 相关的参数.

`backend/app/src/main/resources/application.yml`
`backend/app/src/test/resources/application.yml`
`frontend/travel-assistant/src/api/index.js`