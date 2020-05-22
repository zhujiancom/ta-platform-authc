import Vue from 'vue'
import {
    DEFAULT_COLOR,
    DEFAULT_CONTENT_WIDTH_TYPE,
    DEFAULT_FIXED_HEADER,
    DEFAULT_FIXED_HEADER_HIDDEN, DEFAULT_FIXED_SIDEMENU,
    DEFAULT_LAYOUT_MODE, DEFAULT_MULTI_PAGE, DEFAULT_THEME,
    SIDEBAR_TYPE
} from "../mutation-types";

const app ={
    state: {
        sidebar: {
            opened: true,
            withoutAnimation: false
        },
        device: 'desktop',
        theme: 'dark',
        layout: '',
        contentWidth: '',
        fixedHeader: false,
        fixSiderbar: false,
        autoHideHeader: false,
        color: null,
        weak: false,
        multipage: true //默认多页签模式
    },
    mutations: {
        SET_SIDEBAR_TYPE: (state, type) => {
            state.sidebar.opened = true
            Vue.ls.set(SIDEBAR_TYPE, type)
        },
        CLOSE_SIDEBAR: (state, withoutAnimation) => {
            Vue.ls.set(SIDEBAR_TYPE, true)
            state.sidebar.opened = false
            state.sidebar.withoutAnimation = withoutAnimation
        },
        TOGGLE_FIXED_HEADER: (state, fixed) => {
            Vue.ls.set(DEFAULT_FIXED_HEADER, fixed)
            state.fixedHeader = fixed
        },
        TOGGLE_FIXED_HEADER_HIDDEN: (state, show) => {
            Vue.ls.set(DEFAULT_FIXED_HEADER_HIDDEN, show)
            state.autoHideHeader = show
        },
        TOGGLE_LAYOUT_MODE: (state, layout) => {
            Vue.ls.set(DEFAULT_LAYOUT_MODE, layout)
            state.layout = layout
        },
        TOGGLE_CONTENT_WIDTH: (state, type) => {
            Vue.ls.set(DEFAULT_CONTENT_WIDTH_TYPE, type)
            state.contentWidth = type
        },
        TOGGLE_COLOR: (state, color) => {
            Vue.ls.set(DEFAULT_COLOR, color)
            state.color = color
        },
        TOGGLE_DEVICE: (state, device) => {
            state.device = device
        },
        TOGGLE_THEME: (state, theme) => {
            // setStore('_DEFAULT_THEME', theme)
            Vue.ls.set(DEFAULT_THEME, theme)
            state.theme = theme
        },
        TOGGLE_FIXED_SIDERBAR: (state, fixed) => {
            Vue.ls.set(DEFAULT_FIXED_SIDEMENU, fixed)
            state.fixSiderbar = fixed
        },
        SET_MULTI_PAGE (state, multipageFlag) {
            Vue.ls.set(DEFAULT_MULTI_PAGE, multipageFlag)
            state.multipage = multipageFlag
        }
    },
    actions: {
        setSidebar: ({ commit }, type) => {
            commit('SET_SIDEBAR_TYPE', type)
        },
        CloseSidebar({ commit }, { withoutAnimation }) {
            commit('CLOSE_SIDEBAR', withoutAnimation)
        },
        ToggleFixedHeader({ commit }, fixedHeader) {
            if (!fixedHeader) {
                commit('TOGGLE_FIXED_HEADER_HIDDEN', false)
            }
            commit('TOGGLE_FIXED_HEADER', fixedHeader)
        },
        ToggleFixedHeaderHidden({ commit }, show) {
            commit('TOGGLE_FIXED_HEADER_HIDDEN', show)
        },
        ToggleFixSiderbar({ commit }, fixSiderbar) {
            commit( 'TOGGLE_FIXED_SIDERBAR', fixSiderbar)
        },
        ToggleLayoutMode({ commit }, mode) {
            commit('TOGGLE_LAYOUT_MODE', mode)
        },
        ToggleContentWidth({ commit }, type) {
            commit('TOGGLE_CONTENT_WIDTH', type)
        },
        ToggleTheme({ commit }, theme) {
            commit('TOGGLE_THEME', theme)
        },
        ToggleColor({ commit }, color) {
            commit('TOGGLE_COLOR', color)
        },
        ToggleDevice({ commit }, device) {
            commit('TOGGLE_DEVICE', device)
        },
        ToggleMultipage({ commit }, multipageFlag) {
            commit('SET_MULTI_PAGE', multipageFlag)
        }
    }
}

export default app
