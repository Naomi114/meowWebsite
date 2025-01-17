import { createStore } from 'vuex';

export const store = createStore({
    state() {
        return {
            // 嘗試從 localStorage 讀取購物車資料
            cart: localStorage.getItem('cart') ? JSON.parse(localStorage.getItem('cart')) : []
        };
    },
    mutations: {
        addToCart(state, product) {
            const found = state.cart.find(item => item.id === product.id);
            if (found) {
                found.quantity++;
            } else {
                state.cart.push({ ...product, quantity: 1, selected: false });
            }
            // Update localStorage after adding item
            localStorage.setItem('cart', JSON.stringify(state.cart));
        },
        removeFromCart(state, id) {
            const index = state.cart.findIndex(item => item.id === id);
            if (index > -1) {
                state.cart.splice(index, 1);
            }
            // Update localStorage after removing item
            localStorage.setItem('cart', JSON.stringify(state.cart));
        },
        toggleSelect(state, id) {
            const item = state.cart.find(item => item.id === id);
            if (item) {
                item.selected = !item.selected;
            }
            // Update localStorage after toggling select
            localStorage.setItem('cart', JSON.stringify(state.cart));
        },
        clearCart(state) {
            state.cart = [];
            // Update localStorage after clearing cart
            localStorage.setItem('cart', JSON.stringify(state.cart));
        },
        checkout(state) {
            // 保留已勾選商品
            console.log('Proceeding to payment with selected items', state.cart.filter(item => item.selected));
            // 清空已勾選的商品
            state.cart = state.cart.filter(item => !item.selected);
            // Update localStorage after checkout
            localStorage.setItem('cart', JSON.stringify(state.cart));
        }
    },
    actions: {
        addToCart({ commit }, product) {
            commit('addToCart', product);
        },
        removeFromCart({ commit }, id) {
            commit('removeFromCart', id);
        },
        toggleSelect({ commit }, id) {
            commit('toggleSelect', id);
        },
        checkout({ commit }) {
            commit('checkout');
        },
        clearCart({ commit }) {
            commit('clearCart');
        }
    }
});
