<sec:authorize access="!isAuthenticated()">
    <div class="container">

        <div class="jumbotron" style="margin-top: 20px;">
            <h1>DZStore</h1>
            <p class="lead">
                Devcolibri - это сервис предоставляющий всем желающим возможность обучаться программированию.
            </p>
            <p><a class="btn btn-lg btn-success" href="<c:url value="/login" />" role="button">Войти</a></p>
        </div>

        <div class="footer">
            <p>© Devcolibri 2014</p>
        </div>

    </div>
</sec:authorize>
