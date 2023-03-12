class SecurityChecker {

    constructor() {
        this._isSecured = null;
    }

    async isSecured() {
        if (this._isSecured !== null) {
            return this._isSecured;
        }

        this._isSecured = await $.ajax({
            type: 'GET',
            url: '/secured'
        });

        return this._isSecured;
    }
}

async function secure() {
    $('.loggedIn').hide();
    $('.loggedOut').hide();
    let isSecured = await new SecurityChecker().isSecured();
    let classToShow = isSecured ? '.loggedIn' : '.loggedOut';
    $(classToShow).show();

    if (isSecured) {
        let username = await $.ajax({
            type: 'GET',
            url: "/logged-user/details"
        });
        $('#myProfile').text(username.username);
    }
}

secure();