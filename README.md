# Примеры для курса "Разработчик Java" в Otus.ru

Группа 2017-04.2

### Автор 
Nikita Chernetsov (Никита Чернецов)

n.chernetsov86@gmail.com

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
