#!/usr/bin/python3
from flask import Flask, render_template, request
from flask_mysqldb import MySQL

app = Flask(__name__)
app.config['MYSQL_HOST'] = 'mysql'
app.config['MYSQL_USER'] = 'web'
app.config['MYSQL_PASSWORD'] = 'web'
mysql = MySQL(app)


@app.route('/syslog-ng')
def syslog():
    app.config['MYSQL_DB'] = 'syslog'
    cur = mysql.connection.cursor()
    cur.execute("SELECT * FROM init ORDER BY datetime DESC;")
    data = cur.fetchall()
    if data:
        count = cur.rowcount
        return render_template('syslog-ng.html', count=count, data=data)
    else:
        return 'failure - no data'

@app.route('/ntop-ng')
def ntop():
    app.config['MYSQL_DB'] = 'ntopng'
    cur = mysql.connection.cursor()
    cur.execute("SELECT * FROM ntopng.flowsv4, ntopng.flowsv6;")
    data = cur.fetchall()
    if data:
        count = cur.rowcount
        return render_template('ntop-ng.html', count=count, data=data)
    else:
        return 'failure - no data'

@app.route('/stats')
def stats():
#    app.config['MYSQL_DB'] = 'syslog'

### Fetch the current init size
    ixs = mysql.connection.cursor()
    ixs.execute("select * from information_schema.tables where table_schema like 'syslog%';")
    table_size = ixs.fetchall()

### Get the current line count from the init database
    irc = mysql.connection.cursor()
    irc.execute("select * from performance_schema.events_statements_current;")
    statements_current = irc.fetchall()

    etc = mysql.connection.cursor()
    etc.execute("select * from performance_schema.events_transactions_current;")
    transactions_current = etc.fetchall()

#    avs = mysql.connection.cursor()
#    avs.execute("SELECT SUM(index_length+data_length) AS total_bytes FROM INFORMATION_SCHEMA.tables WHERE table_schema=? AND table_name LIKE "syslogs_archive_%";")
#    archive_size = avs.fetchall
#
#    init = mysql.connection.cursor()
#    init.execute("SELECT SUM(index_length+data_length) AS total_bytes FROM INFORMATION_SCHEMA.tables WHERE table_schema=? AND table_name LIKE "init";")
#    init_size = init.fetchall
#
    if ixs and irc:
        return render_template('stats.html', table_size=table_size, statements_current=statements_current, transactions_current=transactions_current)
    else:
        return 'failure - no data'

if __name__ == '__main__':
    app.run(host='0.0.0.0')
