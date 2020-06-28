import Home from '@/module/home/page/home.vue';
import PageList from '@/module/cms/page/page_list.vue';
import PageAdd from '@/module/cms/page/page_add.vue';
import PageEdit from '@/module/cms/page/page_edit.vue';

export default [{
  path: '/',
  component: Home,
  name: 'CMS',  // 菜单名称
  hidden: false,  // 是否隐藏
  // 定义下拉列表
  children: [
    {path: '/cms/page/list',name:'页面列表', component: PageList,hidden: false},
    {path: '/cms/page/add',name:'新增页面', component: PageAdd,hidden: true},
    {path: '/cms/page/edit/:pageId',name:'修改页面', component: PageEdit,hidden: true},
  ]
}
/*,
  {
    path: '/login',
    component: Login,
    name: 'Login',
    hidden: true
  }*/
]
