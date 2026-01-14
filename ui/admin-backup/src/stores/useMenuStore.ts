import { create } from 'zustand'
import type { MenuItem } from '@/types/menu'
import { getUserMenus } from '@/api/menu'

interface MenuState {
  menus: MenuItem[]
  permissions: string[]
  loading: boolean
  fetched: boolean
  fetchMenus: () => Promise<void>
  hasPermission: (permission: string) => boolean
  reset: () => void
}

// 提取所有权限标识
const extractPermissions = (menus: MenuItem[]): string[] => {
  const permissions: string[] = []
  const traverse = (items: MenuItem[]) => {
    items.forEach((item) => {
      if (item.permission) {
        permissions.push(item.permission)
      }
      if (item.children?.length) {
        traverse(item.children)
      }
    })
  }
  traverse(menus)
  return permissions
}

export const useMenuStore = create<MenuState>((set, get) => ({
  menus: [],
  permissions: [],
  loading: false,
  fetched: false,

  fetchMenus: async () => {
    if (get().fetched || get().loading) return
    set({ loading: true })
    try {
      const menus = await getUserMenus()
      const permissions = extractPermissions(menus)
      set({ menus, permissions, fetched: true })
    } catch (error) {
      console.error('获取菜单失败:', error)
      set({ fetched: true })
    } finally {
      set({ loading: false })
    }
  },

  hasPermission: (permission: string) => {
    return get().permissions.includes(permission)
  },

  reset: () => set({ menus: [], permissions: [], fetched: false }),
}))
