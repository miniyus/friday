# Friday

-   save web page link
    -   first save host
    -   using keyword(query string key) and search value
    -   and input summary and description
-   keyword search
    -   saved web page's link search by keyword or summary, description
-   galleries
    -   like images tab in google search
-   bookmarks
    -   save web page as alias
-   users
    -   roles
    -   authorities

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

#### ERD

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
