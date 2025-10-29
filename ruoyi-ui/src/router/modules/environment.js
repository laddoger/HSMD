// src/router/modules/environment.js
export default {
    path: '/environment',
    component: () => import('@/layout'), // 若依主 layout
    redirect: '/environment/sectors',
    name: 'Environment',
    meta: {
        title: '环境参数',
        icon: 'el-icon-s-data' // 可换成任何已支持图标
    },
    children: [
        {
            path: 'sectors',
            component: () => import('@/views/environment/sectors/index'), // 你已创建的页面
            name: 'EnvironmentSectors',
            meta: { title: '扇区视图', noCache: true }
        }
    ]
}
