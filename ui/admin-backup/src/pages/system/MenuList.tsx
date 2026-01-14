import { useState, useEffect } from 'react'
import { Table, Button, Space, Modal, Form, Input, InputNumber, Select, message, Popconfirm } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import { getMenuTree, createMenu, updateMenu, deleteMenu } from '@/api/menu'
import type { MenuItem } from '@/types/menu'
import type { ColumnsType } from 'antd/es/table'

const MenuList: React.FC = () => {
  const [loading, setLoading] = useState(false)
  const [data, setData] = useState<MenuItem[]>([])
  const [modalOpen, setModalOpen] = useState(false)
  const [editingMenu, setEditingMenu] = useState<MenuItem | null>(null)
  const [form] = Form.useForm()

  const fetchData = async () => {
    setLoading(true)
    try {
      const res = await getMenuTree()
      setData(res)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchData()
  }, [])

  const handleAdd = (parentId = 0) => {
    setEditingMenu(null)
    form.resetFields()
    form.setFieldsValue({ parentId, type: 1, visible: 1, sort: 0 })
    setModalOpen(true)
  }

  const handleEdit = (record: MenuItem) => {
    setEditingMenu(record)
    form.setFieldsValue(record)
    setModalOpen(true)
  }

  const handleDelete = async (id: number) => {
    await deleteMenu(id)
    message.success('删除成功')
    fetchData()
  }

  const handleSubmit = async () => {
    const values = await form.validateFields()
    if (editingMenu) {
      await updateMenu({ ...values, id: editingMenu.id })
      message.success('更新成功')
    } else {
      await createMenu(values)
      message.success('创建成功')
    }
    setModalOpen(false)
    fetchData()
  }

  const columns: ColumnsType<MenuItem> = [
    { title: '菜单名称', dataIndex: 'name', width: 200 },
    { title: '路径', dataIndex: 'path' },
    { title: '权限标识', dataIndex: 'permission' },
    { 
      title: '类型', 
      dataIndex: 'type', 
      width: 80,
      render: (v) => ['目录', '菜单', '按钮'][v] 
    },
    { title: '排序', dataIndex: 'sort', width: 80 },
    {
      title: '操作',
      width: 200,
      render: (_, record) => (
        <Space>
          {record.type !== 2 && (
            <Button type="link" icon={<PlusOutlined />} onClick={() => handleAdd(record.id)}>
              添加
            </Button>
          )}
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

  return (
    <div>
      <div className="flex justify-between mb-4">
        <h2 className="text-xl font-bold">菜单管理</h2>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => handleAdd(0)}>
          新增菜单
        </Button>
      </div>

      <Table
        rowKey="id"
        loading={loading}
        columns={columns}
        dataSource={data}
        pagination={false}
      />

      <Modal
        title={editingMenu ? '编辑菜单' : '新增菜单'}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
        width={500}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="parentId" hidden>
            <Input />
          </Form.Item>
          <Form.Item name="name" label="菜单名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="type" label="类型" rules={[{ required: true }]}>
            <Select options={[
              { value: 0, label: '目录' },
              { value: 1, label: '菜单' },
              { value: 2, label: '按钮' },
            ]} />
          </Form.Item>
          <Form.Item name="path" label="路径">
            <Input />
          </Form.Item>
          <Form.Item name="permission" label="权限标识">
            <Input placeholder="如: system:user:list" />
          </Form.Item>
          <Form.Item name="icon" label="图标">
            <Input placeholder="如: user, setting" />
          </Form.Item>
          <Form.Item name="sort" label="排序">
            <InputNumber min={0} />
          </Form.Item>
          <Form.Item name="visible" label="是否显示">
            <Select options={[
              { value: 1, label: '显示' },
              { value: 0, label: '隐藏' },
            ]} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default MenuList
