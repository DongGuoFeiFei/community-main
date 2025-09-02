import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'

dayjs.locale('zh-cn')

/**
 * 获取日期范围
 */
export const getDateRange = (type = 'today') => {
    const now = dayjs()

    switch (type) {
        case 'today':
            return {
                start: now.startOf('day'),
                end: now.endOf('day')
            }
        case 'yesterday':
            return {
                start: now.subtract(1, 'day').startOf('day'),
                end: now.subtract(1, 'day').endOf('day')
            }
        case 'thisWeek':
            return {
                start: now.startOf('week'),
                end: now.endOf('week')
            }
        case 'lastWeek':
            return {
                start: now.subtract(1, 'week').startOf('week'),
                end: now.subtract(1, 'week').endOf('week')
            }
        case 'thisMonth':
            return {
                start: now.startOf('month'),
                end: now.endOf('month')
            }
        case 'lastMonth':
            return {
                start: now.subtract(1, 'month').startOf('month'),
                end: now.subtract(1, 'month').endOf('month')
            }
        default:
            return {
                start: now.startOf('day'),
                end: now.endOf('day')
            }
    }
}

/**
 * 计算年龄
 */
export const calculateAge = (birthDate) => {
    if (!birthDate) return null

    const birth = dayjs(birthDate)
    const now = dayjs()
    return now.diff(birth, 'year')
}

/**
 * 判断是否为同一天
 */
export const isSameDay = (date1, date2) => {
    if (!date1 || !date2) return false
    return dayjs(date1).isSame(dayjs(date2), 'day')
}

/**
 * 判断是否为同一周
 */
export const isSameWeek = (date1, date2) => {
    if (!date1 || !date2) return false
    return dayjs(date1).isSame(dayjs(date2), 'week')
}

/**
 * 判断是否为同一月
 */
export const isSameMonth = (date1, date2) => {
    if (!date1 || !date2) return false
    return dayjs(date1).isSame(dayjs(date2), 'month')
}

/**
 * 获取月份列表
 */
export const getMonthList = (startDate, endDate) => {
    const start = dayjs(startDate)
    const end = dayjs(endDate)
    const months = []

    let current = start.startOf('month')

    while (current.isBefore(end) || current.isSame(end, 'month')) {
        months.push(current.format('YYYY-MM'))
        current = current.add(1, 'month')
    }

    return months
}

/**
 * 工作日判断
 */
export const isWorkday = (date) => {
    const day = dayjs(date).day()
    return day !== 0 && day !== 6 // 0是周日，6是周六
}

/**
 * 节假日判断（简单版本，实际项目需要更复杂的逻辑）
 */
export const isHoliday = (date) => {
    const formatted = dayjs(date).format('MM-DD')
    const holidays = ['01-01', '05-01', '10-01'] // 元旦、劳动节、国庆节

    return holidays.includes(formatted)
}

/**
 * 格式化工具类
 * 提供文件大小、时间、数字等的格式化功能
 */

/**
 * 格式化文件大小
 * @param {number} bytes 文件大小（字节）
 * @param {number} decimals 保留小数位数
 * @returns {string} 格式化后的文件大小
 */
export const formatFileSize = (bytes, decimals = 2) => {
    if (bytes === 0 || bytes === null || bytes === undefined) return '0 B'

    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))

    return parseFloat((bytes / Math.pow(k, i)).toFixed(decimals)) + ' ' + sizes[i]
}

/**
 * 格式化时间
 * @param {Date|string|number} date 时间对象、时间字符串或时间戳
 * @param {string} format 格式化模板
 * @returns {string} 格式化后的时间字符串
 */
export const formatTime = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
    if (!date) return ''

    let d
    if (date instanceof Date) {
        d = date
    } else if (typeof date === 'string') {
        // 处理ISO格式字符串
        d = new Date(date)
        // 如果解析失败，尝试处理其他格式
        if (isNaN(d.getTime())) {
            d = new Date(date.replace(/-/g, '/'))
        }
    } else if (typeof date === 'number') {
        d = new Date(date)
    } else {
        return ''
    }

    // 检查日期是否有效
    if (isNaN(d.getTime())) return ''

    const year = d.getFullYear()
    const month = d.getMonth() + 1
    const day = d.getDate()
    const hours = d.getHours()
    const minutes = d.getMinutes()
    const seconds = d.getSeconds()

    const padZero = (num) => (num < 10 ? '0' + num : num)

    return format
        .replace('YYYY', year)
        .replace('YY', String(year).slice(-2))
        .replace('MM', padZero(month))
        .replace('M', month)
        .replace('DD', padZero(day))
        .replace('D', day)
        .replace('HH', padZero(hours))
        .replace('H', hours)
        .replace('mm', padZero(minutes))
        .replace('m', minutes)
        .replace('ss', padZero(seconds))
        .replace('s', seconds)
}

/**
 * 相对时间格式化（如：刚刚、1分钟前、2小时前等）
 * @param {Date|string|number} date 时间
 * @returns {string} 相对时间字符串
 */
export const formatRelativeTime = (date) => {
    if (!date) return ''

    const d = parseDate(date)
    if (!d) return ''

    const now = new Date()
    const diff = now - d
    const minute = 60 * 1000
    const hour = 60 * minute
    const day = 24 * hour
    const week = 7 * day
    const month = 30 * day
    const year = 365 * day

    if (diff < minute) {
        return '刚刚'
    } else if (diff < hour) {
        const minutes = Math.floor(diff / minute)
        return `${minutes}分钟前`
    } else if (diff < day) {
        const hours = Math.floor(diff / hour)
        return `${hours}小时前`
    } else if (diff < week) {
        const days = Math.floor(diff / day)
        return `${days}天前`
    } else if (diff < month) {
        const weeks = Math.floor(diff / week)
        return `${weeks}周前`
    } else if (diff < year) {
        const months = Math.floor(diff / month)
        return `${months}个月前`
    } else {
        const years = Math.floor(diff / year)
        return `${years}年前`
    }
}

/**
 * 聊天消息时间格式化
 * 今天显示时间，昨天显示"昨天"，更早显示日期
 * @param {Date|string|number} date 时间
 * @returns {string} 格式化后的时间
 */
export const formatChatTime = (date) => {
    if (!date) return ''

    const d = parseDate(date)
    if (!d) return ''

    const now = new Date()
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
    const yesterday = new Date(today)
    yesterday.setDate(yesterday.getDate() - 1)

    if (d >= today) {
        return formatTime(d, 'HH:mm')
    } else if (d >= yesterday) {
        return '昨天 ' + formatTime(d, 'HH:mm')
    } else if (d.getFullYear() === now.getFullYear()) {
        return formatTime(d, 'MM-DD HH:mm')
    } else {
        return formatTime(d, 'YYYY-MM-DD')
    }
}

/**
 * 格式化数字（添加千位分隔符）
 * @param {number} num 数字
 * @param {number} decimals 保留小数位数
 * @returns {string} 格式化后的数字
 */
export const formatNumber = (num, decimals = 0) => {
    if (num === null || num === undefined) return '0'

    const number = parseFloat(num)
    if (isNaN(number)) return '0'

    const fixedNum = number.toFixed(decimals)
    const parts = fixedNum.split('.')
    parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',')

    return parts.join('.')
}

/**
 * 格式化百分比
 * @param {number} value 数值（0-1之间）
 * @param {number} decimals 保留小数位数
 * @returns {string} 百分比字符串
 */
export const formatPercent = (value, decimals = 2) => {
    if (value === null || value === undefined) return '0%'

    const number = parseFloat(value)
    if (isNaN(number)) return '0%'

    return (number * 100).toFixed(decimals) + '%'
}

/**
 * 格式化持续时间（毫秒转换为可读格式）
 * @param {number} milliseconds 毫秒数
 * @returns {string} 格式化后的持续时间
 */
export const formatDuration = (milliseconds) => {
    if (milliseconds === null || milliseconds === undefined) return '0秒'

    const ms = parseInt(milliseconds)
    if (isNaN(ms) || ms < 0) return '0秒'

    const seconds = Math.floor(ms / 1000)
    const minutes = Math.floor(seconds / 60)
    const hours = Math.floor(minutes / 60)
    const days = Math.floor(hours / 24)

    if (days > 0) {
        return `${days}天${hours % 24}小时`
    } else if (hours > 0) {
        return `${hours}小时${minutes % 60}分钟`
    } else if (minutes > 0) {
        return `${minutes}分钟${seconds % 60}秒`
    } else {
        return `${seconds}秒`
    }
}

/**
 * 解析日期对象
 * @param {Date|string|number} date 日期
 * @returns {Date|null} 解析后的日期对象
 */
const parseDate = (date) => {
    if (date instanceof Date) {
        return date
    } else if (typeof date === 'string') {
        // 尝试解析ISO格式
        let d = new Date(date)
        if (!isNaN(d.getTime())) return d

        // 尝试解析其他格式
        d = new Date(date.replace(/-/g, '/'))
        if (!isNaN(d.getTime())) return d

        return null
    } else if (typeof date === 'number') {
        const d = new Date(date)
        return isNaN(d.getTime()) ? null : d
    }
    return null
}

/**
 * 获取时间范围描述
 * @param {Date} startDate 开始时间
 * @param {Date} endDate 结束时间
 * @returns {string} 时间范围描述
 */
export const formatTimeRange = (startDate, endDate) => {
    if (!startDate || !endDate) return ''

    const start = parseDate(startDate)
    const end = parseDate(endDate)
    if (!start || !end) return ''

    const isSameDay = start.toDateString() === end.toDateString()

    if (isSameDay) {
        return `${formatTime(start, 'YYYY-MM-DD')} ${formatTime(start, 'HH:mm')} - ${formatTime(end, 'HH:mm')}`
    } else {
        return `${formatTime(start, 'YYYY-MM-DD HH:mm')} - ${formatTime(end, 'YYYY-MM-DD HH:mm')}`
    }
}

/**
 * 格式化文件类型图标
 * @param {string} fileName 文件名
 * @returns {string} 文件类型图标类名
 */
export const getFileTypeIcon = (fileName) => {
    if (!fileName) return 'el-icon-document'

    const extension = fileName.split('.').pop().toLowerCase()

    const iconMap = {
        // 图片
        'jpg': 'el-icon-picture',
        'jpeg': 'el-icon-picture',
        'png': 'el-icon-picture',
        'gif': 'el-icon-picture',
        'bmp': 'el-icon-picture',
        'svg': 'el-icon-picture',
        'webp': 'el-icon-picture',

        // 文档
        'pdf': 'el-icon-document',
        'doc': 'el-icon-document',
        'docx': 'el-icon-document',
        'xls': 'el-icon-document',
        'xlsx': 'el-icon-document',
        'ppt': 'el-icon-document',
        'pptx': 'el-icon-document',
        'txt': 'el-icon-document',
        'md': 'el-icon-document',

        // 压缩文件
        'zip': 'el-icon-folder',
        'rar': 'el-icon-folder',
        '7z': 'el-icon-folder',
        'tar': 'el-icon-folder',
        'gz': 'el-icon-folder',

        // 音频
        'mp3': 'el-icon-headset',
        'wav': 'el-icon-headset',
        'flac': 'el-icon-headset',
        'aac': 'el-icon-headset',

        // 视频
        'mp4': 'el-icon-video-play',
        'avi': 'el-icon-video-play',
        'mov': 'el-icon-video-play',
        'wmv': 'el-icon-video-play',
        'flv': 'el-icon-video-play',

        // 代码
        'js': 'el-icon-document',
        'ts': 'el-icon-document',
        'html': 'el-icon-document',
        'css': 'el-icon-document',
        'java': 'el-icon-document',
        'py': 'el-icon-document',
        'php': 'el-icon-document',
        'json': 'el-icon-document',
        'xml': 'el-icon-document'
    }

    return iconMap[extension] || 'el-icon-document'
}

/**
 * 格式化货币金额
 * @param {number} amount 金额
 * @param {string} currency 货币符号
 * @param {number} decimals 小数位数
 * @returns {string} 格式化后的金额
 */
export const formatCurrency = (amount, currency = '¥', decimals = 2) => {
    if (amount === null || amount === undefined) return `${currency}0.00`

    const number = parseFloat(amount)
    if (isNaN(number)) return `${currency}0.00`

    return currency + formatNumber(number, decimals)
}


