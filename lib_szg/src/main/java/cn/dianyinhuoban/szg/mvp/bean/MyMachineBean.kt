package cn.dianyinhuoban.szg.mvp.bean

data class MyMachineBean(
    val activeCount: String,
    val count: String,
    val `data`: List<MachineItemBean>,
    val nonActiveCount: String
)

