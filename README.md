Использованные технологии:
Spring-boot | Spring-web-mvc / spring-di
JPA(Hibernate) + HSQLDB (embedded file mode)
Thymeleaf + Layout dialect
Jquery+Bootstrap

Это приложение является вариантом spring-boot приложения со встроенным apache-tomcat

Запуск приложения java -Dserver-port=<tomcat-http-port>-jar TestTask-1.0-SNAPSHOT.jar <путь к каталогу, который будет использоваться HSQLD для хранения данных>

Путь к каталогу может быть как относительным, относительно текущего рабочего каталога, так и абсолютным

Запуск с параметрами по умолчанию:

java -jar TestTask-1.0-SNAPSHOT.jar

Для хранения данных HSQLDB будет создан каталог database в текущей рабочей директории

Порт томката по умолчанию - 8080


Для доступа к приложению открыть браузер и ввести в адресной строке localhost:8080 или 127.0.0.1:8080


