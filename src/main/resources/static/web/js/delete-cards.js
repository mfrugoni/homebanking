Vue.createApp({
    data() {
        return {
            cards: [],
            activeCards: [],
            cardId: 0 ,
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current/cards")
                .then((response) => {
                        //get active cards info:
                        this.cards = response.data;
                        console.log(this.cards);
                        this.activeCards = this.cards.filter(card => card.isActive == true);
                        console.log(this.activeCards);

                })
                .catch((error) => {
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        formatDate: function (date) {
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
        erase: function (event) {
            event.preventDefault();
            if (this.cardId == 0) {
                this.errorMsg = "You must select a card to delete";
                this.errorToats.show();
            } else {
                let config = {
                    headers: {
                        'content-type': 'application/x-www-form-urlencoded'
                    }
                }
                axios.patch(`/api/clients/current/cards?id=${this.cardId}`, config)
                    .then(response => window.location.href = "/web/cards.html")
                    .catch((error) => {
                        this.errorMsg = error.response.data;
                        this.errorToats.show();
                    })
            }
        },
//        checkInput: function (){
//                if (this.cardId == 0) {
//                   this.errorMsg = "You must select a card to delete";
//                   this.errorToats.show();
//                } else {
//                this.modal.show();
//                }
//        },

    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
//        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.getData();
    }
}).mount('#app')