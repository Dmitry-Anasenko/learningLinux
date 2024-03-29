# Learning Linux

Learning Linux - обучающая программа по Linux-системам, разработанная Анасенко Дмитрием Андреевичем в качестве дипломного проекта для ХИИК СибГУТИ. Является
первым проектом указанного разработчика.

Программа предоставляет функционал по созданию и редактированию файлов с теорией и тестами (для преподавателей) и чтению теории и прохождению тестов (для
студентов). Для защиты файлов от несанкционированного доступа используется шифрование AES.

# Установка
Исходные коды собираются в исполняемый jar-файл. Для успешного запуска программы необходимо в ту же папку, в которой находится jar, скопировать каталог `defaults`.

Каталог `defaults` содержит файлы по умолчанию, включая теорию и тесты. При первом запуске программы они будут автоматически скопированы и зашифрованы, а сам каталог defaults будет удален.

# Работа с файловой системой
Программа создает и использует следующие файлы:
- каталог *theory* - содержит файлы с расширением **.llh** (Learning Linux tHeory). Содержимое файлов представляет собой код на языке Markdown в
зашифрованном виде. Внутри данного каталога содержится подкаталог *images*, включающий в себя изображения для файлов с теорией;
- каталог *tests* - содержит файлы с расширением **.lle** (Learning Linux tEst). Формат файла в незашифрованном виде:
>Вопрос  
>Ответ №1  
>Ответ №2  
>Ответ №3  
>Ответ №4  
>Номера правильных ответов без разделителей

Например:

>Какие из перечисленных дистрибутивов GNU/Linux основаны на Debian?  
>Ubuntu  
>Fedora  
>Kali Linux  
>Manjaro  
>13

После данного блока следует разделитель в виде пустой строки, затем следующий блок с вопросом.
- файл *config.llc* (Learning Linux Config) - конфигурационный файл, включающий в себя ключ шифрования для подтверждения пароля.

Шифрование выполняется только для файлов в каталогах *theory* и *tests*.

# Зависимости
Исходные коды включают в себя стандартные зависимости JavaFX, включая javafx-web, а также библиотеки commonmark-java версии 0.20.0 и jsoup версии 1.15.4.
