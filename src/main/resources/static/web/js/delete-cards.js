Vue.createApp({
    data() {
        return {
            cards: [],
            cardNumber: "none",
            errorToats: null,
            errorMsg: null,
        }
    },
    methods: {
        getData: function () {
                    axios.get("/api/clients/current/cards")
                        .then((response) => {
                            //get cards info
                            //this.cards = response;
                            //console.log(this.cards);
                            console.log(response.data);
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
        delete: function (event) {
            event.preventDefault();
            if (this.cardNumber == "none") {
                this.errorMsg = "You must select a card number";
                this.errorToats.show();
            } else {
                let config = {
                    headers: {
                        'content-type': 'application/x-www-form-urlencoded'
                    }
                }
                axios.post(`/api/clients/current/cards/del?cardNumber=${this.cardNumber}`, config)
                    .then(response => window.location.href = "/web/cards.html")
                    .catch((error) => {
                        this.errorMsg = error.response.data;
                        this.errorToats.show();
                    })
            }
        }
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
    }
}).mount('#app')