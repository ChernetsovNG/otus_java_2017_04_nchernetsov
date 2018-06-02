# Домашние задания по курсу "Разработчик Java" в Otus.ru

Группа 2017-04.2

### Автор 
Nikita Chernetsov (Никита Чернецов)

n.chernetsov86@gmail.com

#### Краткое описание домашних заданий:

* HW1

Сборка архива при помощи Maven с обфускацией

* HW2

Подсчёт занимаемой объектами памяти при помощи обхода дерева объекта
с использованием java.lang.instrument.Instrumentation

* HW2-additional

Подсчёт занимаемой объектами памяти при помощи выделения памяти под объекты
и подсчёта разницы свободной памяти (с промежуточными сборками мусора)

* HW3

Самописная реализация ArrayList (список на основе массива)

* HW4

Исследование работы различных сборщиков мусора

* HW5

Самописный тестовый фреймворк наподобие JUnit 4 (с аннотациями @Test, @Before, @After)

* HW7

Симулятор банкомата с реализациями паттернов Memento, Strategy (2 алгоритма списания
денег: жадный и алгоритм динамического программирования) и Builder

* HW8

Метод сериализации объекта в JSON. Поддержаны примитивные типы, обёртки, массивы,
коллекции, внутренние объекты. Для тестов результаты сравнивания с GSON

* HW9

Самописный ORM, работающий на основе анализа аннотаций JPA и механизма Reflection. 
Пример создания ConnectionFactory с пулом соединений к БД

* HW10

Пример работы с Hibernate, с использованием паттернов DBService и DAO

* HW11

Самописный CacheEngine на основе SoftReference, с evict элементов по времени жизни и 
по времени последнего обращения

* HW12

Страница для получения данных DBCache. Получение времени сервера при помощи Ajax

* HW13

Пример создания бинов Spring из контекста веб-приложения при инициализации сервлетов

* HW14

* HW15

* HW16


#### Комментарий по ДЗ HW2:

Чтобы запустить приложение из командной строки, необходимо указать путь к классу-агенту.
Например, из папки target запуск будет выглядеть как:

java -javaagent:./java-2017-04-HW2-1.0-SNAPSHOT.jar -jar java-2017-04-HW
2-1.0-SNAPSHOT.jar 

Можно добавить аналогичную строку в Edit Configurations -> VM Optionst:

-javaagent:HW2/target/java-2017-04-HW2-1.0-SNAPSHOT.jar

и запускаться из IDEA.

#### Комментарий по ДЗ HW4:

Результаты сравнения сборщиков мусора на моей домашней конфигурации в файле
HW4/results.txt.
