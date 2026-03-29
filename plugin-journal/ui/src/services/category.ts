import { axiosInstance } from '@halo-dev/api-client';

const API_PATH = '/apis/journal.hippo.com/v1alpha1/journalcategories';

export interface JournalCategory {
  apiVersion: string;
  kind: string;
  metadata: {
    name: string;
    version?: number;
    creationTimestamp?: string;
  };
  spec: {
    name: string;
    nameEn?: string;
    sort?: number;
  };
}

export function listCategories() {
  return axiosInstance.get<{ items: JournalCategory[] }>(API_PATH);
}

export function getCategory(name: string) {
  return axiosInstance.get<JournalCategory>(`${API_PATH}/${name}`);
}

export function createCategory(data: JournalCategory) {
  return axiosInstance.post<JournalCategory>(API_PATH, data);
}

export function updateCategory(name: string, data: JournalCategory) {
  return axiosInstance.put<JournalCategory>(`${API_PATH}/${name}`, data);
}

export function deleteCategory(name: string) {
  return axiosInstance.delete(`${API_PATH}/${name}`);
}
