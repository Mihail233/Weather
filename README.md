# Weather

Веб-приложение для просмотра текущей погоды в выбранных городах. Пользователь регистрируется, находит локации через OpenWeather и сохраняет их в личный список. На главной странице отображаются температура, ощущаемая температура, влажность, координаты и иконка погодного состояния.

Проект реализован на **Spring MVC без Spring Boot**. Сервер формирует HTML с помощью Thymeleaf; приложение собирается в **WAR** и разворачивается во внешнем servlet-контейнере.

## Возможности

- Регистрация, вход и выход из аккаунта.
- Хеширование паролей с BCrypt.
- Авторизация через cookie и сессии, сохранённые в базе данных.
- Поиск до пяти локаций по названию с отображением страны, региона и координат.
- Добавление и удаление локаций из личного списка.
- Получение текущей погоды по координатам в метрических единицах.
- Проверка принадлежности локации пользователю при удалении.
- Валидация форм и отображение страниц ошибок.
- Миграции базы данных при запуске приложения.

## Технологии

Версии приведены по конфигурации проекта, а не как рекомендация использовать последние релизы.

| Компонент | Технология |
| --- | --- |
| Язык и сборка | Java 25, Gradle Wrapper 9.6.1 |
| Веб-слой | Spring Web MVC 7.0.7, Jakarta Servlet API 6.1 |
| HTML | Thymeleaf 3.1.5.RELEASE (`thymeleaf-spring6`), Bootstrap 5.3.0 |
| Доступ к данным | Spring Data JPA 4.0.5, Hibernate ORM 7.4.0.Final |
| База данных | PostgreSQL в Docker, JDBC-драйвер 42.7.11 |
| Миграции | Flyway 12.10.0 |
| Валидация | Jakarta Validation, Hibernate Validator 9.1.3.Final |
| Вспомогательные библиотеки | Lombok, MapStruct, Jackson, jBCrypt |
| HTTP-клиент | `java.net.http.HttpClient` |
| Тестирование | JUnit Jupiter 6.0.3, Spring Test, H2, WireMock |

## Локальный запуск

### 1. Подготовить окружение

Потребуются:

- JDK 25 для сборки и запуска Tomcat.
- Docker Desktop или Docker Engine для запуска PostgreSQL; устанавливать сервер PostgreSQL на компьютер отдельно не требуется.
- Apache Tomcat 11.x: он поддерживает используемый проектом Servlet API 6.1. См. [таблицу совместимости Tomcat](https://tomcat.apache.org/whichversion.html).
- Собственный API-ключ OpenWeather с доступом к [Geocoding API](https://openweathermap.org/api/geocoding-api) и [Current Weather Data](https://openweathermap.org/api/current).
- Доступ в интернет для загрузки зависимостей и запросов погоды. Bootstrap и погодные иконки также загружаются из внешних источников.

Отдельно устанавливать Gradle не требуется: используйте `gradlew` из корня проекта. Для Windows заменяйте `./gradlew` на `gradlew.bat`.

### 2. Запустить PostgreSQL в Docker

При локальной разработке PostgreSQL запускалась в Docker-контейнере, а само приложение — в Tomcat через IntelliJ IDEA. `.env` для этого не нужен: параметры контейнера передаются непосредственно в команде запуска.

Запустите Docker Desktop или Docker Engine. Ниже — пример создания нового локального окружения с образом `postgres:17`. Перед выполнением замените `YOUR_DATABASE_PASSWORD` своим паролем; этот же пароль потребуется в настройках приложения.

```bash
docker run -d \
  --name weather-postgres \
  -p 127.0.0.1:5432:5432 \
  -e POSTGRES_DB=weather \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD='YOUR_DATABASE_PASSWORD' \
  -v weather-postgres-data:/var/lib/postgresql/data \
  postgres:17
```

Команда приведена для Bash/Zsh. База `weather` создаётся автоматически при первой инициализации. Именованный том `weather-postgres-data` хранит данные между запусками контейнера; путь тома в примере рассчитан на PostgreSQL 17. Порт опубликован только на локальном интерфейсе компьютера.

Проверьте готовность базы:

```bash
docker exec weather-postgres pg_isready -U postgres -d weather
```

Дождитесь ответа `accepting connections`. При проблемах посмотрите журнал:

```bash
docker logs weather-postgres
```

Если контейнер уже создан, для повторного запуска используйте `docker start weather-postgres`, а для остановки — `docker stop weather-postgres`. Повторно выполнять `docker run` с тем же именем не нужно. Не удаляйте том с данными, если хотите сохранить пользователей и локации.

Если порт 5432 занят, в примере можно использовать `127.0.0.1:5433:5432`; тогда укажите порт 5433 в JDBC URL приложения. Так как Tomcat работает вне Docker, подключение идёт через `localhost` и опубликованный порт, а не через имя контейнера.

Переменные `POSTGRES_DB`, `POSTGRES_USER` и `POSTGRES_PASSWORD` применяются при инициализации пустого каталога данных. Их изменение не перенастраивает уже существующую базу. Подробнее: [официальный Docker-образ PostgreSQL](https://hub.docker.com/_/postgres).

Используйте новую отдельную базу: при запуске Flyway применяет миграции. Не направляйте приложение на базу с посторонними таблицами.

### 3. Настроить приложение

Основной способ настройки в текущем проекте — файл `app/src/main/resources/application.properties`. Создавать `.env` не требуется. Для базы из предыдущего шага конфигурация может выглядеть так:

```properties
db.driver=org.postgresql.Driver
db.url=jdbc:postgresql://localhost:5432/weather
db.username=postgres
db.password=YOUR_DATABASE_PASSWORD

hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.show_sql=true
hibernate.format_sql=true
hibernate.hbm2ddl.auto=validate
hibernate.highlight_sql=true
hibernate.default_schema=public

openWeather.api.key=YOUR_OPENWEATHER_API_KEY
openWeather.api.base-url=https://api.openweathermap.org
```

Замените значения-заглушки своими. Имя базы, пользователь и пароль должны совпадать с настройками контейнера из предыдущего шага; JDBC URL должен содержать опубликованный на компьютере порт. Пример выше использует базу `weather` и порт 5432.

Не публикуйте реальные пароли и API-ключи. Файл `application.properties` попадает в WAR, поэтому архив также содержит указанные в нём настройки. `.env` автоматически не загружается: отдельного загрузчика этого файла в приложении нет.

После изменения `application.properties` пересоберите и заново разверните WAR.

#### Переменные окружения без `.env` — необязательный вариант

Вместо изменения значений в файле можно передать настройки непосредственно процессу Tomcat через **Startup/Connection → Pass environment variables** его конфигурации запуска в IntelliJ IDEA. В некоторых версиях интерфейса поле называется **Environment variables**. Это переменные окружения процесса, а не `.env`-файл.

| Переменная | Настройка приложения | Пример значения |
| --- | --- | --- |
| `DB_URL` | `db.url` | `jdbc:postgresql://localhost:5432/weather` |
| `DB_USERNAME` | `db.username` | `postgres` |
| `DB_PASSWORD` | `db.password` | `YOUR_DATABASE_PASSWORD` |
| `OPENWEATHER_API_KEY` | `openWeather.api.key` | `YOUR_OPENWEATHER_API_KEY` |
| `OPENWEATHER_API_BASE_URL` | `openWeather.api.base-url` | `https://api.openweathermap.org` |

В стандартном окружении Spring эти переменные имеют приоритет над значениями из подключённого через `@PropertySource` файла. Остальные параметры можно оставить в `application.properties`. Имена приведены для текущих `@Value` в проекте: например, используется `OPENWEATHER_API_KEY`, без дополнительного подчёркивания между `OPEN` и `WEATHER`.

Добавляйте переменные именно в конфигурацию **Tomcat**, а не только в Gradle: сборка WAR и запуск приложения — разные процессы. После изменения переменных перезапустите Tomcat; пересборка WAR не нужна, если содержимое проекта не менялось.

Не сохраняйте реальные секреты в общих конфигурациях запуска, попадающих в Git. Переопределение переменной окружения также не удаляет секрет, если он уже записан в `application.properties` внутри WAR.

Подробнее: [источники настроек Spring](https://docs.spring.io/spring-framework/reference/core/beans/environment.html) и [сопоставление имён переменных окружения](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/env/SystemEnvironmentPropertySource.html).

### 4. Собрать WAR

Из корня проекта:

```bash
./gradlew :app:war
```

Архив появится по пути:

```text
app/build/libs/app-1.0-SNAPSHOT.war
```

Задача `war` не запускает тесты. Команды тестирования и особенности их конфигурации описаны ниже.

### 5. Настроить окружение запуска в IntelliJ IDEA

Проект можно запускать через **Tomcat Server → Local**, без `.env` и без Spring Boot. В имеющейся локальной конфигурации используется Tomcat 11.0.18 и контекст приложения `/`; ниже показано, как настроить такой вариант заново.

1. Откройте корень проекта в IntelliJ IDEA и импортируйте его как Gradle-проект.
2. В **File → Project Structure → Project SDK** выберите JDK 25. В **Settings → Build, Execution, Deployment → Build Tools → Gradle** выберите Gradle Wrapper и Gradle JVM 25.
3. Откройте **Run → Edit Configurations**, добавьте **Tomcat Server → Local** и укажите каталог установленного Tomcat 11.x. Если такого пункта нет, используйте ручное развёртывание из следующего раздела; интеграция Tomcat доступна не во всех комплектациях IDE.
4. На вкладке **Server** выберите JRE/JDK 25. Если используете параметры прямо из `application.properties`, дополнительные переменные не нужны. Для необязательного варианта выше откройте **Startup/Connection**, выберите режим **Run** или **Debug**, включите **Pass environment variables** и добавьте имена и значения из таблицы.
5. На вкладке **Deployment** добавьте собранный на предыдущем шаге `app/build/libs/app-1.0-SNAPSHOT.war` как **External Source**. Установите **Application context** в `/`, чтобы адреса не содержали имени WAR.
6. Настройте HTTPS для используемого экземпляра Tomcat. В поле стартового URL укажите `/sign-in` на его HTTPS-адресе, например `https://localhost:8443/sign-in`, если выбран порт 8443.
7. Убедитесь, что контейнер PostgreSQL запущен и база принимает подключения, сохраните конфигурацию и нажмите **Run**. Проверьте успешное выполнение миграций в консоли Tomcat.

В таком варианте регистрация доступна по `/sign-up`, а погодные карточки — по `/weather`, без дополнительного префикса. После изменения кода или `application.properties` снова выполните `./gradlew :app:war` и обновите развёртывание. Чтобы IDE собирала WAR перед каждым запуском, можно добавить Gradle-задачу `:app:war` в **Before launch**.

Справка по полям конфигурации: [Tomcat Server в IntelliJ IDEA](https://www.jetbrains.com/help/idea/run-debug-configuration-tomcat-server.html).

### 6. Альтернатива: развернуть WAR в Tomcat вне IDE

1. Настройте Tomcat на использование JDK 25.
2. Настройте HTTPS в Tomcat либо перед ним через reverse proxy.
3. Разверните полученный WAR через Tomcat Manager или поместите его в каталог `webapps` отдельного экземпляра Tomcat. Например, имя `weather.war` задаст контекст `/weather`.
4. Запустите Tomcat и проверьте его журнал: Flyway должен применить миграции, после чего Hibernate проверит схему.
5. Откройте страницу регистрации относительно контекста приложения: `/sign-up`.

Например, при HTTPS на порту 8443 и контексте `/weather`:

```text
https://localhost:8443/weather/sign-up
```

Если оставить имя `app-1.0-SNAPSHOT.war`, стандартный контекст будет `/app-1.0-SNAPSHOT`. Порт и HTTPS необходимо настроить самостоятельно: проект не создаёт встроенный сервер или TLS-сертификат.

**Важно:** cookie `user_session` устанавливается с флагами `HttpOnly` и `Secure`. Для корректной авторизации используйте HTTPS; на обычном HTTP поведение зависит от браузера и исключений для localhost. Отключение `Secure` для публичного размещения не является решением.

Подробности развёртывания: [документация Tomcat](https://tomcat.apache.org/tomcat-11.0-doc/deployer-howto.html).

### 7. Проверить работу в браузере

1. Зарегистрируйте пользователя: логин и пароль должны содержать от 5 до 100 символов, подтверждение пароля должно совпадать.
2. После регистрации откроется страница `/weather` относительно контекста приложения.
3. Найдите город, например `Moscow`, и добавьте нужный результат.
4. Убедитесь, что появилась погодная карточка. Обновление страницы повторно запрашивает текущую погоду.
5. Проверьте удаление карточки, выход и повторный вход.

Отдельного обработчика для корневого маршрута `/` нет: начинайте с `/sign-up` или `/sign-in`.

## Маршруты

Все пути указаны **относительно контекста приложения**. Это HTML-страницы и обработчики форм, а не JSON REST API.

| Метод | Путь | Назначение | Нужна сессия |
| --- | --- | --- | --- |
| GET | `/sign-up` | Форма регистрации | Нет |
| POST | `/sign-up` | Регистрация: `username`, `password`, `repeatedPassword` | Нет |
| GET | `/sign-in` | Форма входа | Нет |
| POST | `/sign-in` | Вход: `username`, `password` | Нет |
| POST | `/sign-out` | Удаление сессии и cookie | Да |
| GET | `/weather` | Погода для сохранённых локаций | Да |
| GET | `/location/search` | Страница поиска | Да |
| POST | `/location/search` | Поиск: `locationName` | Да |
| POST | `/location/add` | Добавление: `name`, `country`, `latitude`, `longitude` | Да |
| POST | `/location/delete` | Удаление: `locationId` | Да |

## Структура проекта

```text
Weather/
├── app/
│   ├── build.gradle
│   └── src/
│       ├── main/
│       │   ├── java/org/weather/
│       │   │   ├── common/service/  # Интерфейсы сервисов
│       │   │   ├── config/          # Spring MVC, JPA, Flyway, servlet initializer
│       │   │   ├── data/entity/     # Пользователи, сессии, локации
│       │   │   ├── data/repository/ # Spring Data JPA
│       │   │   ├── exception/       # Исключения приложения
│       │   │   ├── service/         # Бизнес-логика и OpenWeather-клиент
│       │   │   ├── util/            # Работа с cookie и формами
│       │   │   └── web/             # Контроллеры, DTO, мапперы, обработчики, interceptor
│       │   └── resources/
│       │       ├── application.properties
│       │       ├── db/             # SQL-миграции
│       │       ├── static/css/     # Стили
│       │       └── templates/      # Thymeleaf-шаблоны
│       └── test/                   # Тесты и тестовая конфигурация
├── gradle/wrapper/
├── gradlew
└── settings.gradle
```

Запрос проходит через `PrivateEndpointInterceptor`, контроллер и сервис. Сервисы обращаются к JPA-репозиториям либо OpenWeather; результат преобразуется в DTO и передаётся HTML-шаблону.

## Хранение данных и сессий

| Таблица | Основные поля | Назначение |
| --- | --- | --- |
| `Users` | `id`, `login`, `password` | Уникальный логин и BCrypt-хеш пароля |
| `Sessions` | `id`, `user_id`, `expires_at` | Серверная сессия пользователя |
| `Locations` | `id`, `name`, `user_id`, `latitude`, `longitude` | Сохранённые пользователем локации |

Один пользователь может иметь несколько сессий и локаций. Срок действия сессии — **1 час**; он задаётся в `SessionServiceImpl`. Просроченная сессия удаляется при обращении к защищённому маршруту. Продление сессии при каждом запросе не реализовано.

Flyway читает миграции из `classpath:db`:

- `V1__init_tables.sql` — таблицы, внешние ключи и уникальный индекс логина.
- `V2__change_latitude_and_longitude.sql` — изменение точности координат.

Hibernate работает в режиме `validate`: он проверяет структуру, а не создаёт её вместо миграций.

## Интеграция с OpenWeather

- Поиск: `GET /geo/1.0/direct`, параметры `q`, `limit=5`, `appid`.
- Текущая погода: `GET /data/2.5/weather`, параметры `lat`, `lon`, `appid`, `units=metric`.

Погода запрашивается последовательно для каждой сохранённой локации при открытии страницы. Кеширование и прогноз на несколько дней не реализованы. При большом количестве карточек учитывайте время ответа API и лимиты своего ключа.

## Тестирование

В проекте есть два тестовых класса:

- `OpenWeatherServiceTest` — поиск локаций и обработка ответов 400/500 с помощью WireMock.
- `UserControllerTest` — несмотря на название, проверяет сервисы и репозитории: связь пользователя с сессией, повторную регистрацию логина и истечение сессии.

**Перед запуском проверьте изоляцию тестового окружения.** `DatabaseTestConfig` сканирует весь пакет `org.weather`, включая основную конфигурацию БД и Flyway. Это может подключить рабочую конфигурацию вместо ожидаемой H2. Не запускайте такой контекст с доступом к ценной базе; настройте изолированную тестовую БД и набор конфигураций.

В `OpenWeatherTestConfig` загружается `test.properties`, но сервис читает `openWeather.api.key`, а тест — `test.openWeather.api.key`. Для тестирования задайте в этом файле обе настройки с одинаковым фиктивным значением. Реальный ключ и запросы к OpenWeather для WireMock не нужны.

После подготовки тестового окружения:

```bash
./gradlew :app:test
```

Отчёт: `app/build/reports/tests/test/index.html`.

Полная сборка, включающая тесты:

```bash
./gradlew :app:build
```
