# Список персонала фирмы

Данное приложение реализует список персонала фирмы с возможностью добавления, удаления и изменения сотрудников, а также их сортировки по различным критериям. Все данные сотрудников хранятся в XML-файле, и работа с этим файлом осуществляется с использованием технологии DOM.

## Описание задачи

В фирме все работники делятся на следующие типы:

1. **Работник**:
    - ФИО
    - Дата рождения
    - Дата принятия на работу

2. **Менеджер**:
    - ФИО
    - Дата рождения
    - Дата принятия на работу
    - Список работников, находящихся в подчинении данного менеджера

3. **Другие** (руководство, секретари и т.д.):
    - ФИО
    - Дата рождения
    - Дата принятия на работу
    - Текстовое описание сотрудника

## Основные функции программы

- Добавление сотрудника в список.
- Удаление сотрудника из списка.
- Изменение типа сотрудника (например, работник может стать менеджером).
- Привязка сотрудника к менеджеру.
- Сортировка списка сотрудников по фамилиям и датам принятия на работу.
- Чтение и запись данных в XML файл.
- Загрузка данных из текстового файла при добавлении сотрудника.

## Реализация

- Приложение консольное, выполнено на языке Java.
- Для работы с XML используется технология DOM.
- Входные данные для добавления сотрудника считываются из текстового файла.

### Структура программы:

1. **Employee** - базовый класс для всех типов сотрудников.
2. **Manager** - наследует от `Employee`, добавляет возможность хранения списка подчиненных.
3. **OtherEmployee** - класс для других сотрудников с дополнительным описанием.
4. **Company** - класс для управления списком сотрудников, сортировки и сохранения/загрузки данных в XML.
5. **Main** - класс для запуска и проверки работоспособности программы.
