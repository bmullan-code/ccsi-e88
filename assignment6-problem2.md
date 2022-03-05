


# Problem 2

Query1: get count of unique URLs per hour 
```
Key: 1 YYYY-MM-DD:HH Column Family:counts Column Field:unique_urls
```
Query2: get count of unique visitors per URL per hour 
```
Key: 2 URL YYYY-MM-DD:HH Column Family: Counts Column Field:unique_visitors
```
Query3: get count of events/clicks per URL per hour
```
Key: 3 URL YYYY-MM-DD:HH Column Family: Counts Column Field:clicks
```

Query4: get count of unique URLs by country by hour for a  specified time range [t1-t2]
```
Key: 4 CountryCode YYYY-MM-DD:HH Column Family:counts Column Field:unique_urls 
```
Query5: get top 5 URLs by smallest avg daily TTFB, per each day in the input data set 
```
Key: 5 YYYY-MM-DD Rank URL Column Family:stats Column Field:ttfb
```

## Create Script
```
create 'batch_views_hw6', 'counts','stats'

put 'batch_views_hw6', '1 2022-01-27:04','counts:unique_urls','187'
put 'batch_views_hw6', '1 2022-01-25:06','counts:unique_urls','189'
put 'batch_views_hw6', '1 2022-01-26:03','counts:unique_urls','187'
put 'batch_views_hw6', '1 2022-01-26:10','counts:unique_urls','150'

put 'batch_views_hw6', '2 http://example.com/?url=067 2022-01-27:04','counts:unique_visitors','187'
put 'batch_views_hw6', '2 http://example.com/?url=067 2022-01-25:06','counts:unique_visitors','189'
put 'batch_views_hw6', '2 http://example.com/?url=067 2022-01-26:03','counts:unique_visitors','187'
put 'batch_views_hw6', '2 http://example.com/?url=024 2022-01-26:10','counts:unique_visitors','150'

put 'batch_views_hw6', '3 http://example.com/?url=067 2022-01-27:04','counts:clicks','187'
put 'batch_views_hw6', '3 http://example.com/?url=067 2022-01-25:06','counts:clicks','189'
put 'batch_views_hw6', '3 http://example.com/?url=067 2022-01-26:03','counts:clicks','187'
put 'batch_views_hw6', '3 http://example.com/?url=024 2022-01-26:10','counts:clicks','150'

put 'batch_views_hw6', '4 IE 2022-01-27:04','counts:unique_urls','187'
put 'batch_views_hw6', '4 IE 2022-01-25:06','counts:unique_urls','189'
put 'batch_views_hw6', '4 US 2022-01-26:03','counts:unique_urls','187'
put 'batch_views_hw6', '4 US 2022-01-26:10','counts:unique_urls','150'

put 'batch_views_hw6', '5 2022-01-25 1 http://example.com/?url=032','stats:ttfb','0.1552'
put 'batch_views_hw6', '5 2022-01-25 2 http://example.com/?url=067','stats:ttfb','0.3142'
put 'batch_views_hw6', '5 2022-01-25 3 http://example.com/?url=161','stats:ttfb','0.5118'
put 'batch_views_hw6', '5 2022-01-25 4 http://example.com/?url=024','stats:ttfb','0.9025'
put 'batch_views_hw6', '5 2022-01-25 5 http://example.com/?url=057','stats:ttfb','0.9028'


```

# Demonstrate Results

## Query 1

### Get all query 1 results.

```
scan 'batch_views_hw6', { STARTROW => '1', ENDROW => '2' }
ROW                                    COLUMN+CELL
 1 2022-01-25:06                       column=counts:unique_urls, timestamp=1646500543682, value=189
 1 2022-01-26:03                       column=counts:unique_urls, timestamp=1646500543685, value=187
 1 2022-01-26:10                       column=counts:unique_urls, timestamp=1646500543689, value=150
 1 2022-01-27:04                       column=counts:unique_urls, timestamp=1646500543675, value=18

```

### Get query 1 results for '1 2022-01-26:10'
```
hbase(main):017:0> get 'batch_views_hw6','1 2022-01-26:10'
COLUMN                                 CELL
 counts:unique_urls                    timestamp=1646500543689, value=150
1 row(s)
Took 0.0050 seconds
```

## Query 2

### Get all query 2 results
```
hbase(main):018:0> scan 'batch_views_hw6', { STARTROW => '2', ENDROW => '3' }
ROW                                    COLUMN+CELL
 2 http://example.com/?url=024 2022-01 column=counts:unique_visitors, timestamp=1646500543706, value=150
 -26:10
 2 http://example.com/?url=067 2022-01 column=counts:unique_visitors, timestamp=1646500543699, value=189
 -25:06
 2 http://example.com/?url=067 2022-01 column=counts:unique_visitors, timestamp=1646500543702, value=187
 -26:03
 2 http://example.com/?url=067 2022-01 column=counts:unique_visitors, timestamp=1646500543696, value=187
 -27:04
4 row(s)
Took 0.0161 seconds
```
### Query 2: Get query 2 results for a particular url
```
hbase(main):019:0> scan 'batch_views_hw6', { FILTER => "PrefixFilter('2 http://example.com/?url=067')" }
ROW                                    COLUMN+CELL
 2 http://example.com/?url=067 2022-01 column=counts:unique_visitors, timestamp=1646500543699, value=189
 -25:06
 2 http://example.com/?url=067 2022-01 column=counts:unique_visitors, timestamp=1646500543702, value=187
 -26:03
 2 http://example.com/?url=067 2022-01 column=counts:unique_visitors, timestamp=1646500543696, value=187
 -27:04
3 row(s)
Took 0.0541 seconds

```

### Query 2 : Get the values for a particular url and hour
```
hbase(main):020:0> get 'batch_views_hw6', '2 http://example.com/?url=067 2022-01-27:04'
COLUMN                                 CELL
 counts:unique_visitors                timestamp=1646500543696, value=187
1 row(s)
Took 0.0076 seconds

```

## Query 3

### Query 3 : Get all query 3 results
```

hbase(main):024:0* scan 'batch_views_hw6', { STARTROW => '3', ENDROW => '4' }
ROW                                    COLUMN+CELL
 3 http://example.com/?url=024 2022-01 column=counts:clicks, timestamp=1646500543718, value=150
 -26:10
 3 http://example.com/?url=067 2022-01 column=counts:clicks, timestamp=1646500543712, value=189
 -25:06
 3 http://example.com/?url=067 2022-01 column=counts:clicks, timestamp=1646500543715, value=187
 -26:03
 3 http://example.com/?url=067 2022-01 column=counts:clicks, timestamp=1646500543709, value=187
 -27:04
4 row(s)
Took 0.0190 seconds

```

### Query 3 : Get the clicks for a particular url and hour
```
hbase(main):025:0> get 'batch_views_hw6', '3 http://example.com/?url=067 2022-01-27:04',{ COLUMN => "counts:clicks" }
COLUMN                                 CELL
 counts:clicks                         timestamp=1646500543709, value=187
1 row(s)
Took 0.0143 seconds

```

## Query 4

### Query 4 : Get all query 4 results
```

hbase(main):030:0* scan 'batch_views_hw6', { STARTROW => '4', ENDROW => '5' }
ROW                                    COLUMN+CELL
 4 IE 2022-01-25:06                    column=counts:unique_urls, timestamp=1646500543732, value=189
 4 IE 2022-01-27:04                    column=counts:unique_urls, timestamp=1646500543721, value=187
 4 US 2022-01-26:03                    column=counts:unique_urls, timestamp=1646500543736, value=187
 4 US 2022-01-26:10                    column=counts:unique_urls, timestamp=1646500543743, value=150
4 row(s)
Took 0.0138 seconds
```

### Query 4 : Get results for a country
```
hbase(main):031:0> scan 'batch_views_hw6', { FILTER => "PrefixFilter('4 IE')" }
ROW                                    COLUMN+CELL
 4 IE 2022-01-25:06                    column=counts:unique_urls, timestamp=1646500543732, value=189
 4 IE 2022-01-27:04                    column=counts:unique_urls, timestamp=1646500543721, value=187
2 row(s)
Took 0.0163 seconds
```

### Query 4 : Get result for a country and hour
```
hbase(main):033:0> get 'batch_views_hw6', '4 IE 2022-01-27:04',{ COLUMN => "counts:unique_urls" }
COLUMN                                 CELL
 counts:unique_urls                    timestamp=1646500543721, value=187
1 row(s)
Took 0.0098 seconds
```

## Query 5

### Query 5 : Get all results for query 5
```
hbase(main):037:0* scan 'batch_views_hw6', { STARTROW => '5', ENDROW => '6' }
ROW                                    COLUMN+CELL
 5 2022-01-25 1 http://example.com/?ur column=stats:ttfb, timestamp=1646500543746, value=0.1552
 l=032
 5 2022-01-25 2 http://example.com/?ur column=stats:ttfb, timestamp=1646500543750, value=0.3142
 l=067
 5 2022-01-25 3 http://example.com/?ur column=stats:ttfb, timestamp=1646500543752, value=0.5118
 l=161
 5 2022-01-25 4 http://example.com/?ur column=stats:ttfb, timestamp=1646500543756, value=0.9025
 l=024
 5 2022-01-25 5 http://example.com/?ur column=stats:ttfb, timestamp=1646500543759, value=0.9028
 l=057
 ```
 
 ### Query 5 : Get top ranked url for a day
 ```
hbase(main):038:0>  scan 'batch_views_hw6', { FILTER => "PrefixFilter('5 2022-01-25 1')" }
ROW                                    COLUMN+CELL
 5 2022-01-25 1 http://example.com/?ur column=stats:ttfb, timestamp=1646500543746, value=0.1552
 l=032
1 row(s)
Took 0.0237 seconds
```


 
