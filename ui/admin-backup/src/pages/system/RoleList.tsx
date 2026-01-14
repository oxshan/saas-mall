import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, message, Popconfirm, Tree } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, KeyOutlined } from '@ant-design/icons'
import { getRoleList, createRole, updateRole, deleteRole, assignMenus, getRoleMenuIds } from '@/api/role'
import { getMenuTree } from '@/api/menu'
import type { RoleInfo } from '@/api/role'
import type { MenuItem } from '@/types/menu'
import type { ColumnsType } from 'antd/es/table'

const RoleList: React.FC = () => {
  const [loading, setLoading] = useState(false)
  const [data, setData] = useState<RoleInfo[]>([])
  const [total, setTotal] = useState(0)
  const [pageNum, setPageNum] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [modalOpen, setModalOpen] = useState(false)
  const [permModalOpen, setPermModalOpen] = useState(false)
  const [editingRole, setEditingRole] = useState<RoleInfo | null>(null)
  const [menuTree, setMenuTree] = useState<MenuItem[]>([])
  const [checkedKeys, setCheckedKeys] = useState<number[]>([])
  const [form] = Form.useForm()

  const fetchData = async () => {
    setLoading(true)
    try {
      const res = await getRoleList(pageNum, pageSize)
      setData(res.list)
      setTotal(res.total)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchData()
  }, [pageNum, pageSize])

  const handleAdd = () => {
    setEditingRole(null)
    form.resetFields()
    setModalOpen(true)
  }

  const handleEdit = (record: RoleInfo) => {
    setEditingRole(record)
    form.setFieldsValue(record)
    setModalOpen(true)
  }

  const handleDelete = async (id: number) => {
    await deleteRole(id)
    message.success('删除成功')
    fetchData()
  }

  const handleSubmit = async () => {
    const values = await form.validateFields()
    if (editingRole) {
      await updateRole({ ...values, id: editingRole.id })
      message.success('更新成功')
    } else {
      await createRole(values)
      message.success('创建成功')
    }
    setModalOpen(false)
    fetchData()
  }

  // 分配权限
  const handleAssignPerm = async (record: RoleInfo) => {
    setEditingRole(record)
    const [tree, keys] = await Promise.all([
      getMenuTree(),
      getRoleMenuIds(record.id)
    ])
    setMenuTree(tree)
    setCheckedKeys(keys)
    setPermModalOpen(true)
  }

  const handlePermSubmit = async () => {
    if (!editingRole) return
    await assignMenus(editingRole.id, checkedKeys)
    message.success('权限分配成功')
    setPermModalOpen(false)
  }

  const columns: ColumnsType<RoleInfo> = [
    { title: 'ID', dataIndex: 'id', width: 80 },
    { title: '角色名称', dataIndex: 'name' },
    { title: '角色编码', dataIndex: 'code' },
    { title: '状态', dataIndex: 'status', render: (v) => (v === 1 ? '正常' : '禁用') },
    {
      title: '操作',
      width: 200,
      render: (_, record) => (
        <Space>
          <Button type="link" icon={<KeyOutlined />} onClick={() => handleAssignPerm(record)}>
            权限
          </Button>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>
            编辑
          </Button>
          <Popconfirm title="确定删除?" onConfirm={() => handleDelete(record.id)}>
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ]

  // 转换菜单树为 Tree 组件格式
  const convertTreeData = (items: MenuItem[]): any[] => {
    return items.map((item) => ({
      key: item.id,
      title: item.name,
      children: item.children ? convertTreeData(item.children) : undefined,
    }))
  }

  return (
    <div>
      <div className="flex justify-between mb-4">
        <h2 className="text-xl font-bold">角色管理</h2>
        <Button type="primary" icon={<PlusOutlined />} onClick={handleAdd}>
          新增角色
        </Button>
      </div>

      <Table
        rowKey="id"
        loading={loading}
        columns={columns}
        dataSource={data}
        pagination={{
          current: pageNum,
          pageSize,
          total,
          onChange: (page, size) => { setPageNum(page); setPageSize(size) },
        }}
      />

      <Modal
        title={editingRole ? '编辑角色' : '新增角色'}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="角色名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="code" label="角色编码" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="remark" label="备注">
            <Input.TextArea />
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title="分配权限"
        open={permModalOpen}
        onOk={handlePermSubmit}
        onCancel={() => setPermModalOpen(false)}
      >
        <Tree
          checkable
          checkedKeys={checkedKeys}
          onCheck={(keys) => setCheckedKeys(keys as number[])}
          treeData={convertTreeData(menuTree)}
        />
      </Modal>
    </div>
  )
}

export default RoleList
