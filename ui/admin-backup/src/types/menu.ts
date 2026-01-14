// 菜单类型
export interface MenuItem {
  id: number
  parentId: number
  name: string
  path: string
  component?: string
  icon?: string
  type: number // 0-目录 1-菜单 2-按钮
  permission?: string
  sort: number
  visible: number
  children?: MenuItem[]
}
