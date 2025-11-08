/**
 * 第三方库类型声明文件
 * 如果某些包没有 @types 定义，在这里手动声明
 */

// 如果 pixi-live2d-display 没有类型定义
declare module 'pixi-live2d-display' {
  export const Live2DModel: any
  export class Application {
    constructor(options?: any)
    stage: any
    view: HTMLCanvasElement
  }
}

// Vditor 编辑器
declare module 'vditor' {
  export default class Vditor {
    constructor(id: string, options?: any)
    getValue(): string
    setValue(value: string): void
    destroy(): void
  }
}

// js 文件
declare module '*.js' {
  const content: any
  export default content
}

// 其他没有类型定义的包
declare module '*.scss' {
  const content: { [className: string]: string }
  export default content
}

declare module '*.less' {
  const content: { [className: string]: string }
  export default content
}

declare module '*.png' {
  const value: string
  export default value
}

declare module '*.jpg' {
  const value: string
  export default value
}

declare module '*.svg' {
  const value: string
  export default value
}

declare module '*.json' {
  const value: any
  export default value
}

