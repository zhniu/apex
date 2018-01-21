model create name=LoadBalancingFuzzyPolicy_PolicyModel

schema create name=Adj_CM_type flavour=Java schema=java.util.HashMap<String,String>
schema create name=Adj_KPI_type flavour=Java schema=java.util.HashMap<String,String>
schema create name=Adjacency_4G_Name_type flavour=Java schema=java.lang.String
schema create name=CellA_CM_type flavour=Java schema=java.util.HashMap<String,String>
schema create name=CellA_KPI_type flavour=Java schema=java.util.HashMap<String,String>
schema create name=CellA_type flavour=Java schema=java.lang.String
schema create name=CellB_CM_type flavour=Java schema=java.util.HashMap<String,String>
schema create name=CellB_KPI_type flavour=Java schema=java.util.HashMap<String,String>
schema create name=CellB_type flavour=Java schema=java.lang.String
schema create name=ENABLE_CONGESTION_type flavour=Java schema=java.lang.Boolean
schema create name=FREQ_REL_TYPE_type flavour=Java schema=java.lang.String
schema create name=FooBar_type flavour=Java schema=com.ericsson.apex.sample.sampletypes.sampletypes.Foo
schema create name=HO_EXEC_FAIL_RATE_AB_type flavour=Java schema=java.lang.Float
schema create name=HO_EXEC_FAIL_RATE_BA_type flavour=Java schema=java.lang.Float
schema create name=LOW_CQI_SAMPLES_RATE_HO_AB_type flavour=Java schema=java.lang.Float
schema create name=LOW_CQI_SAMPLES_RATE_HO_BA_type flavour=Java schema=java.lang.Float
schema create name=LOW_PUSCH_SINR_SAMPLES_RATE_HO_AB_type flavour=Java schema=java.lang.Float
schema create name=LOW_PUSCH_SINR_SAMPLES_RATE_HO_BA_type flavour=Java schema=java.lang.Float
schema create name=OCN_AB_type flavour=Java schema=java.lang.Double
schema create name=OCN_BA_type flavour=Java schema=java.lang.Double
schema create name=PDCCH_CCE_HIGH_LOAD_DELTA_type flavour=Java schema=java.lang.Float
schema create name=REL_HO_EXEC_FAIL_RATE_AB_type flavour=Java schema=java.lang.Boolean
schema create name=REL_HO_EXEC_FAIL_RATE_BA_type flavour=Java schema=java.lang.Boolean
schema create name=REL_HO_EXEC_FAIL_RATE_HIGH_AB_type flavour=Java schema=java.lang.Float
schema create name=REL_HO_EXEC_FAIL_RATE_HIGH_BA_type flavour=Java schema=java.lang.Float
schema create name=REL_HO_EXEC_FAIL_RATE_LOW_AB_type flavour=Java schema=java.lang.Float
schema create name=REL_HO_EXEC_FAIL_RATE_LOW_BA_type flavour=Java schema=java.lang.Float
schema create name=REL_NUM_HO_WITH_EVENTS_AB_IN_type flavour=Java schema=java.lang.Float
schema create name=REL_NUM_HO_WITH_EVENTS_AB_type flavour=Java schema=java.lang.Boolean
schema create name=REL_NUM_HO_WITH_EVENTS_BA_IN_type flavour=Java schema=java.lang.Float
schema create name=REL_NUM_HO_WITH_EVENTS_BA_type flavour=Java schema=java.lang.Boolean
schema create name=REL_TOO_EARLY_HO_RATE_AB_type flavour=Java schema=java.lang.Boolean
schema create name=REL_TOO_EARLY_HO_RATE_BA_type flavour=Java schema=java.lang.Boolean
schema create name=REL_TOO_EARLY_HO_RATE_HIGH_AB_type flavour=Java schema=java.lang.Float
schema create name=REL_TOO_EARLY_HO_RATE_HIGH_BA_type flavour=Java schema=java.lang.Float
schema create name=REL_TOO_EARLY_HO_RATE_LOW_AB_type flavour=Java schema=java.lang.Float
schema create name=REL_TOO_EARLY_HO_RATE_LOW_BA_type flavour=Java schema=java.lang.Float
schema create name=REL_TOO_LATE_HO_RATE_AB_type flavour=Java schema=java.lang.Boolean
schema create name=REL_TOO_LATE_HO_RATE_BA_type flavour=Java schema=java.lang.Boolean
schema create name=REL_TOO_LATE_HO_RATE_HIGH_AB_type flavour=Java schema=java.lang.Float
schema create name=REL_TOO_LATE_HO_RATE_HIGH_BA_type flavour=Java schema=java.lang.Float
schema create name=REL_TOO_LATE_HO_RATE_LOW_AB_type flavour=Java schema=java.lang.Float
schema create name=REL_TOO_LATE_HO_RATE_LOW_BA_type flavour=Java schema=java.lang.Float
schema create name=RRC_CONGESTION_DELTA_type flavour=Java schema=java.lang.Float
schema create name=SB_OCN_AB_type flavour=Java schema=java.lang.String
schema create name=SB_OCN_BA_type flavour=Java schema=java.lang.String
schema create name=SET_TYPE_type flavour=Java schema=java.lang.String
schema create name=TOO_EARLY_HO_RATE_AB_type flavour=Java schema=java.lang.Float
schema create name=TOO_EARLY_HO_RATE_BA_type flavour=Java schema=java.lang.Float
schema create name=TOO_LATE_HO_RATE_AB_type flavour=Java schema=java.lang.Float
schema create name=TOO_LATE_HO_RATE_BA_type flavour=Java schema=java.lang.Float
schema create name=TimeStamp_type flavour=Java schema=java.lang.Long

album create name=ExternalBag0 scope=EXTERNAL writable=false schemaName=OCN_AB_type
album create name=ExternalBag1 scope=EXTERNAL writable=false schemaName=CellA_CM_type
album create name=ExternalBag2 scope=EXTERNAL writable=false schemaName=TOO_EARLY_HO_RATE_AB_type
album create name=ExternalBag3 scope=EXTERNAL writable=false schemaName=FooBar_type

album create name=Application scope=APPLICATION writable=true schemaname=OCN_AB_type
