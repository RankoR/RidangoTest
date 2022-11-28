![CI Status](https://github.com/RankoR/RidangoTest/actions/workflows/main.yml/badge.svg)

# Simple test task implementation

Stack I've used:

- Kotlin
- Kotlin Flow
- MVVM
- Hilt for DI
- Retrofit for networking
- Cicerone for simple navigation
- Timber for logging
- KTS syntax for build files
- MockWebServer for a fake web server impl
- MockK
- JUnit

Supported Android versions are 6.0+.

### Some implementation details

#### (missing) multi-modules

I've intentionally used a single-module approach, as it's faster to implement when you develop demo apps. In normal apps I prefer a multi-module approach.

#### Fake web server

Fake web server is implemented in [WebServer.kt](/app/src/main/java/page/smirnov/ridango/data/network/WebServer.kt).

#### Missing required annotation

There is no `required` annotation for the [ticket.proto](/app/src/main/proto/ticket.proto) as it was removed in the version of Protobuf I use.

#### No docs

Unfortunately I don't have time to write KDocs for the code. Just believe me that I do it when it's necessary :)

#### Tests

Pretty much all of the business logic is covered by tests:

- [TicketsRepositoryTest.kt](/app/src/test/java/page/smirnov/ridango/TicketsRepositoryTest.kt)
- [CreateTicketTest.kt](/app/src/test/java/page/smirnov/ridango/CreateTicketTest.kt)
- [MainViewModel.kt](/app/src/androidTest/java/page/smirnov/ridango/MainViewModelTest.kt)

No UI tests here, because it would require to replace the `WebServer` implementation provided by Dagger, and I don't have time to do it right now.

But there is a bonus — [simple CI](/.github/workflows/main.yml) using GitHub Actions.
