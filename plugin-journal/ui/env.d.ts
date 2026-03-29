/// <reference types="vite/client" />
/// <reference types="unplugin-icons/types/vue" />

import 'vue'

declare module 'vue' {
  interface ComponentCustomProperties {
    $formkit: any;
  }
}
