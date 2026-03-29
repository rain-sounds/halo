<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { VButton, VModal, VDropdown, VDropdownItem, VPageHeader, VCard, VSpace, Toast } from '@halo-dev/components'
import { listCategories, createCategory, updateCategory, deleteCategory, type JournalCategory } from '../services/category'
import RiMore2Fill from '~icons/ri/more-2-fill'
import RiAddLine from '~icons/ri/add-line'
import RiFolder2Line from '~icons/ri/folder-2-line'

const categories = ref<JournalCategory[]>([])
const loading = ref(false)

const modalVisible = ref(false)
const modalType = ref<'create' | 'edit'>('create')
const formData = ref({
  name: '',
  nameEn: '',
  sort: 0,
  originalName: '', // For update
  version: 0 // For optimistic locking
})
const isSubmitting = ref(false)

const fetchCategories = async () => {
  loading.value = true
  try {
    const res = await listCategories()
    categories.value = res.data.items || []
    categories.value.sort((a, b) => (a.spec.sort || 0) - (b.spec.sort || 0))
  } catch (e) {
    console.error('Failed to fetch categories', e)
  } finally {
    loading.value = false
  }
}

const openCreateModal = () => {
  modalType.value = 'create'
  formData.value = { name: '', nameEn: '', sort: 0, originalName: '', version: 0 }
  modalVisible.value = true
}

const openEditModal = (category: JournalCategory) => {
  modalType.value = 'edit'
  formData.value = {
    name: category.spec.name,
    nameEn: category.spec.nameEn || '',
    sort: category.spec.sort || 0,
    originalName: category.metadata.name,
    version: category.metadata.version || 0
  }
  modalVisible.value = true
}

const handleSave = async () => {
  if (!formData.value.name) return
  isSubmitting.value = true

  const payload: JournalCategory = {
    apiVersion: 'journal.hippo.com/v1alpha1',
    kind: 'JournalCategory',
    metadata: {
      name: formData.value.name, // Usually metadata.name should be english or pinyin, but we'll use name as id if create
    },
    spec: {
      name: formData.value.name,
      nameEn: formData.value.nameEn,
      sort: Number(formData.value.sort)
    }
  }

  try {
    if (modalType.value === 'create') {
      // Create random name for metadata.name if nameEn is not provided
      payload.metadata.name = formData.value.nameEn ? formData.value.nameEn.toLowerCase().replace(/\s+/g, '-') : 'category-' + Date.now()
      await createCategory(payload)
    } else {
      payload.metadata.name = formData.value.originalName
      payload.metadata.version = formData.value.version // Required for update
      await updateCategory(formData.value.originalName, payload)
    }
    modalVisible.value = false
    Toast.success('保存成功')
    await fetchCategories()
  } catch (e) {
    console.error('Save failed', e)
    Toast.error('保存失败')
  } finally {
    isSubmitting.value = false
  }
}

const handleDelete = async (category: JournalCategory) => {
  if (confirm(`确定要删除类别 ${category.spec.name} 吗？`)) {
    try {
      await deleteCategory(category.metadata.name)
      Toast.success('删除成功')
      await fetchCategories()
    } catch (e) {
      console.error('Delete failed', e)
      Toast.error('删除失败')
    }
  }
}

const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  const yyyy = date.getFullYear()
  const MM = String(date.getMonth() + 1).padStart(2, '0')
  const dd = String(date.getDate()).padStart(2, '0')
  const HH = String(date.getHours()).padStart(2, '0')
  const mm = String(date.getMinutes()).padStart(2, '0')
  return `${yyyy}-${MM}-${dd} ${HH}:${mm}`
}

onMounted(() => {
  fetchCategories()
})
</script>

<template>
  <VPageHeader :title="'期刊类别'">
    <template #icon>
      <RiFolder2Line class="mr-2" />
    </template>
    <template #actions>
      <VButton type="secondary" @click="openCreateModal">
        <template #icon><RiAddLine /></template>
        新建
      </VButton>
    </template>
  </VPageHeader>

  <div class="m-0 md:m-4">
    <VCard :body-class="['!p-0']">
      <template #header>
        <div class="block w-full bg-gray-50 px-4 py-3">
          <div class="text-sm text-gray-500 font-medium">
            {{ categories.length }} 个分类
          </div>
        </div>
      </template>

      <div v-if="loading" class="p-4 text-center text-gray-500">加载中...</div>
      <div v-else-if="categories.length === 0" class="p-4 text-center text-gray-500">暂无数据</div>
      <div v-else class="divide-y divide-gray-100">
        <div v-for="item in categories" :key="item.metadata.name" class="flex items-center justify-between p-4 hover:bg-gray-50 transition-colors">
          <div class="flex flex-col gap-1">
            <div class="text-sm font-medium text-gray-900">{{ item.spec.name }}</div>
            <div class="text-xs text-gray-400">
              /journal/{{ item.spec.nameEn }}
            </div>
          </div>
          <div class="flex items-center gap-6">
            <div class="text-xs text-gray-400 flex items-center gap-4">
              <span>排序：{{ item.spec.sort }}</span>
            </div>
            <div class="text-xs text-gray-400 flex items-center gap-4">
              <span>{{ formatDate(item.metadata.creationTimestamp) || '' }}</span>
            </div>
            <VDropdown>
              <div class="cursor-pointer text-gray-400 hover:text-gray-700 transition-colors">
                <RiMore2Fill class="text-lg" />
              </div>
              <template #popper>
                <VDropdownItem @click="openEditModal(item)">编辑</VDropdownItem>
                <VDropdownItem type="danger" @click="handleDelete(item)">删除</VDropdownItem>
              </template>
            </VDropdown>
          </div>
        </div>
      </div>
    </VCard>
  </div>

  <VModal v-model:visible="modalVisible" :title="modalType === 'create' ? '新建期刊类别' : '编辑期刊类别'" :width="650">
    <FormKit
      v-if="modalVisible"
      id="category-form"
      name="category-form"
      :config="{ validationVisibility: 'submit' }"
      type="form"
      @submit="handleSave"
    >
      <FormKit
        v-model="formData.name"
        label="名称"
        type="text"
        name="name"
        validation="required|length:0,50"
      ></FormKit>

      <FormKit
        v-model="formData.nameEn"
        label="别名 (英文名称)"
        type="text"
        name="nameEn"
        help="通常用于生成分类的固定链接"
      ></FormKit>

      <FormKit
        v-model="formData.sort"
        label="排序"
        type="number"
        name="sort"
      ></FormKit>
    </FormKit>

    <template #footer>
      <VSpace>
        <VButton
          :loading="isSubmitting"
          type="secondary"
          @click="$formkit.submit('category-form')"
        >
          提交 (Ctrl + ↵)
        </VButton>
        <VButton @click="modalVisible = false">
          取消 (Esc)
        </VButton>
      </VSpace>
    </template>
  </VModal>
</template>


