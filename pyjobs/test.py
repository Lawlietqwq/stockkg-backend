import tushare as ts
import time
import pandas as pd
import numpy as np
import datetime


TUSHARE_TOKEN = ''
ts.set_token(TUSHARE_TOKEN)
pro = ts.pro_api()
now = datetime.datetime.now()
end_date = now.strftime("%Y%m%d")
basic_data = pro.stock_basic(exchange='', list_status='L', fields='ts_code,symbol,name,industry')
code_list = basic_data['ts_code']
df = pro.top10_holders(ts_code=code_list[0], start_date=str(int(end_date) - 10000), end_date=end_date)
print(df)