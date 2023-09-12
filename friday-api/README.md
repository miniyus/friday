# Friday: REST API

## Design

### Data Entities

-   User
-   Role
-   Authority
-   Host
-   Search
-   Gallery
-   Bookmark
-   Image

### Business Domain

-   User
    -   Role
        -   Authority
-   Host
    -   Search
        -   Image
-   Gallery
    -   Image
-   Bookmark
    -   Image

#### Domain

### Architecture

#### Hexagonal Architecture

-   [common](./src/main/java/com/miniyus/friday/common/README.md)
-   [infrastructure](./src/main/java/com/miniyus/friday/infrastructure/README.md)
-   [domain](./src/main/java/com/miniyus/friday/README.md)
