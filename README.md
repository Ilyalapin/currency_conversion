Проект «Обмен валют»


REST API для описания валют и обменных курсов. Позволяет просматривать и редактировать списки валют и обменных курсов, и совершать расчёт конвертации произвольных сумм из одной валюты в другую.
Веб-интерфейс для проекта не подразумевается.

Приложение написано на языке Java в ООП-стиле с использованием:


Паттерна MVC

Maven

Servlets

REST API

SQLite

Tomcat 10

Deploy in VRS (Ubuntu 22.04)

Техническое задание проекта: https://zhukovsd.github.io/java-backend-learning-course/Projects/CurrencyExchange/



Реализованные запросы:


GET-запросы

/currencies – получение списка всех валют

/currency/RUB – получение конкретной валюты

/exchangeRates – получение списка всех обменных курсов

/exchangeRate/USDRUB – Получение конкретного обменного курса

/exchange?from=USD&to=RUB&amount=10 – перевод определённого количества средств из одной валюты в другую


POST-запросы

добавление новой валюты в базу
/currencies?name=Argentine Pesor&code=ARS&sign=$

добавление нового обменного курса в базу
/exchangeRates?baseCurrencyCode=USD&targetCurrencyCode=RUB&rate=77


PATCH-запрос

/exchangeRate/USDRUB?rate=89 – обновление существующего в базе обменного курса


Ссылка на приложение: http://45.8.248.74:8080/currency_conversion/currencies
