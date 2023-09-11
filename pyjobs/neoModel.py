from py2neo.ogm import *

class Company(Model):
    __primarykey__ = "ts_code"

    ts_code = Property()
    ann_date = Property()

    holder_name = RelatedFrom("Holder", "持有")



class Holder(Model):
    __primarykey__ = "holder_name"

    holder_name = Property()
    hold_amount = Property()
    hold_ratio = Property()

    holding_company = RelatedTo(Company)
