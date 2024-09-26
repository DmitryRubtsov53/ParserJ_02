### Задание:

1. Сделать простой парсинг json'a с вложенным массивом элементов. в результате должно получиться 3 строки в мапе. 
Ключ - название тега, известное заранее. Значение взять из соответствующего поля json'a.
Интересующие значения:
"productId"
"messageId"
"accountingDate"
"registerType"
"restIn"
2. Вытащить конфигурацию мапинга полей в yaml для возможности универсального использования
3. Написать тест, который будет сравнивать значения полей json с константами.
4. '*' Добавить чтение json'a из кафки
5. '*' Добавить запись значений из получившейся мапы в БД
6. Внести изменение в логику приложения так, что бы в результате в БД должно так же, 
   как и раньше записываться 3 строки, но уже содержащих:
   * "productId"
   * "messageId"
   * "accountingDate"
   * "registerType"
   * "restIn"
   * UID - уникальный ИД
   * InsertDate - датаВремя вставки строки в БД.
#### При этом отличаться строки будут только полями:  
   * "registerType"
   * "restIn"
   * UID - уникальный ИД
   * InsertDate - датаВремя вставки строки в БД. 
