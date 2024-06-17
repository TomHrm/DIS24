import psycopg2
import csv


def handle_sql_input():
    query = """SELECT s.Name, c.Name, r.Name, n.Name FROM shop s
                                 INNER JOIN city c ON s.shopid = c.cityid
                                 INNER JOIN region r ON c.regionid = r.regionid
                                 INNER JOIN country n ON r.countryid = n.countryid;"""
    cursor.execute(query)
    rows = cursor.fetchall()
    for row in rows:
        print("Found this" + str(row) + " in table Shop, City, Region, Country")
        query = """INSERT INTO Shop (Name, City, Region, Country) 
        VALUES (%s, %s, %s, %s);"""
        cursor_warehouse.execute(query, (row[0], row[1], row[2], row[3]))

    query = """SELECT a.name, a.price, g.name, f.name, c.name FROM article a
                               INNER JOIN productgroup g ON a.productgroupid = g.productgroupid
                               INNER JOIN productfamily f ON g.productfamilyid = f.productfamilyid
                               INNER JOIN productcategory c ON f.productcategoryid = c.productcategoryid;"""
    cursor.execute(query)
    rows = cursor.fetchall()
    for row in rows:
        print("Found this" + str(row) + " in table Article, ProductGroup, ProductFamily, ProductCategory")
        query = """INSERT INTO Article (Name,Price, ProductGroupName, ProductFamilyName,ProductCategoryName) 
        VALUES (%s, %s, %s, %s, %s);"""
        cursor_warehouse.execute(query, (row[0], row[1], row[2], row[3], row[4]))

    conn_warehouse.commit()
    cursor.close()
    cursor_warehouse.close()


def check_date_exists(date_string):
    queryDate = """SELECT d.DateId FROM Date d WHERE d.day = %s AND d.month = %s AND d.year = %s;"""
    date_parts = date_string.split('.')
    cursor_warehouse.execute(queryDate, (date_parts[0], date_parts[1], date_parts[2]))
    date_id = cursor_warehouse.fetchone()
    if date_id is None:
        if 1 <= int(date_parts[1]) <= 3:
            quarter = 1
        elif 4 <= int(date_parts[1]) <= 6:
            quarter = 2
        elif 7 <= int(date_parts[1]) <= 9:
            quarter = 3
        else:
            quarter = 4
        queryInsertDate = """INSERT INTO Date(Day, Quarter, Month, Year) VALUES (%s, %s, %s, %s) RETURNING DateId;"""
        cursor_warehouse.execute(queryInsertDate, (date_parts[0], quarter, date_parts[1], date_parts[2]))
        return cursor_warehouse.fetchone()
    else:
        return date_id


def handle_csv():
    print("Now reading the sales.csv file and inserting it into the database")
    csv_file_path = 'sales_utf8.csv'
    with (open(csv_file_path, mode='r') as file):
        csv_reader = csv.reader(file, delimiter=';')

        # Read the header row (if present)
        header = next(csv_reader)
        print(header)

        row_number = 0
        for row in csv_reader:
            print("Reading row: " + str(row_number))
            dateId = check_date_exists(row[0])
            cursor_warehouse.execute("SELECT shopId FROM Shop WHERE Name = '{}';".format(row[1]))
            shopId = cursor_warehouse.fetchone()
            articleIdQuery = "SELECT articleid FROM article WHERE Name = '{}';".format(row[2])
            cursor_warehouse.execute(articleIdQuery)
            articleId = cursor_warehouse.fetchone()

            sale_insert_query = """INSERT INTO Sale (ShopID, ArticleID, DayID, Sold)
             VALUES ({},{},{},{});""".format(shopId[0], articleId[0], dateId[0], row[3])
            cursor_warehouse.execute(sale_insert_query)
            row_number = row_number + 1
        conn_warehouse.commit()




"""
Produces output that the manager can use.
The desired granularity level of each dimension is given by the parameters;
e.g. geo = "country" is the most general
and geo = "shop" is the most fine - grained
granularity level for the geographical dimension.
"""
def analysis(geo: str, time: str, product: str):
    if geo == "shop":
        geo = "name"
    if product == "article":
        product = "name"

    sql_query = """SELECT shop.{0}, date.{1}, article.{2}, SUM(sale.sold) AS sold
                                  FROM shop, sale, article, date
                                  WHERE shop.shopid = sale.shopid 
                                    AND sale.articleid = article.articleid
                                    AND date.dateid = sale.dayid
                                    GROUP BY CUBE(shop.{0}, date.{1}, article.{2});""".format(geo, time, product)
    print(sql_query)
    cursor_warehouse.execute(sql_query)
    output = cursor_warehouse.fetchall()
    with open("output_file.csv", 'w', newline='') as csvfile:
        csv_writer = csv.writer(csvfile)

        # Write the header row
        #csv_writer.writerow([description[0] for description in cursor.description])

        # Write the data rows
        csv_writer.writerows(output)
    print(output)


if __name__ == '__main__':
    # Database connection parameters
    db_params = {
        'database': 'db',
        'user': 'user',
        'password': 'password',
        'host': 'localhost',
        'port': 5433
    }

    db_params_warehouse = {
        'database': 'warehouse',
        'user': 'user',
        'password': 'password',
        'host': 'localhost',
        'port': 5433
    }

    conn = psycopg2.connect(**db_params)
    cursor = conn.cursor()

    conn_warehouse = psycopg2.connect(**db_params_warehouse)
    cursor_warehouse = conn_warehouse.cursor()


    #handle_sql_input()
    #handle_csv()
    #analysis('shop', 'year', 'article')

    # Closing the cursor
    cursor.close()
    cursor_warehouse.close()

    # Closing the connection
    conn.close()
    conn_warehouse.close()
