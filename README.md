# Sportsbook Android App

## Overview
Sportsbook is a modular Android application for browsing, filtering, and managing sports events and favorites. It uses modern Android development practices including Jetpack Compose, Kotlin coroutines, Dagger for dependency injection, and a clean architecture with data, domain, and presentation layers.

## Requirements as defined in the task
- The app should fetch the list of events from an API endpoint. More information about the
  API can be seen in the “API” section below. - `We fetch the data using Retrofit`
- The app should display the list of events in a scrollable list that groups the sport events
  by type, displays the competitors, the countdown timer that indicates when the event is
  scheduled to start and a favorite button. - `Done`
- The app should provide users with the ability to filter events by sport, based on whether
  they have been added as favorites or not. Users can easily access this filter through a
  toggle button located in the header of each sport. When the toggle for a specific sport is
  enabled, the user will exclusively view events for that sport that have been marked as
  favorites. Conversely, when the toggle for a given sport is disabled, the user will see all
  events for that sport, regardless of their favorited status. In summary, enabling the toggle
  tailors the user experience to display only favorite events for the selected sport, while
  disabling it shows all events for that sport. - `The filter was implemented using a backing map which is part of the app state and is responsible for remembering which sport has a filter or not`
- The app should update the countdown timer in real-time, i.e., the timer should decrement
  as time passes and should accurately reflect the time remaining until the event starts. - `The app maintains a global ticker and updates the states of the individual events. In case the event has started (i.e. the timer ran out we display a "Started" label)`
- The app should display an appropriate message if there are no events to display or if
  there is an error fetching the events from the API endpoint. - `In case of an error we display the error message, in case of an empty response we display "No data available"`
- The app should allow user to be able to collapse and expand events per sport. - `Done`
- Expected deliverable is an Android project that can be built and run both in an emulator
  as well as a physical device and should work on SDK21 and above. - `The app was tested on an emulator running API 21`
- The project should be compatible with the latest stable version of Android Studio. Avoid
  any Alpha, Beta and Release Candidate versions - `Done with Android Studio Narwhal 3 Feature Drop`

## Project Structure

### Architecture overview
The project uses the MVVM architectural pattern + a Clean Architecture variant for better separation of concerns.

**Reasoning**
The short answer is - I like it. The longer, more reasonable answer is the following:
We have a unidirectional data flow, meaning the UI is updated using state updates which are triggered by the UI and executed by the viewmodel when a certain action is performed based on the UI action. It's somewhat similar to MVI, in the way that every viewmodel function acts as a sort of intent. So the flow is user event -> state transformation -> UI update.
I chose not to use MVI as in my experience it involves a bit more boilerplate and in the end it achieves a similar result. I did not go with MVC or MVP, partly because they sometimes promote the usage of God Controllers or Presenters and partly because MVVM has been my go to for applications that have a lot of data flowing through and state updates based on the actions.
The Clean Architecture is used because I really like the structure as it's really opinionated as far as sepration goes. This is something I really like as it allows to have smaller components or layers which do their work independently and can be used with completely different components. For example if some UI needs the favorites list, we can use the same data/domain layer.

**Dependency injection** - Dependency injection is provided by Dagger. I chose Dagger and not Hilt (or anything else for that matter) because I'm more comfortable with the control that Dagger provides(pun intended). It does involve a bit more setup and bootstrapping but in the end everything is highly configurable. Hilt is something that's really cool, but I'm not too sure that it provides any significant benefit, let alone performance benefits. Did I need DI in this project necessarily? No, it's small enough that I can get by without it, but it is part of the best practices for Android development and me being kind of OCD about it, I always use it as separating the creation logic not only promotes separation of concers, but it also helps tremendously with testing. (And ultimately fixing issues).

**Models, Entities and DTOs** - In the project I defined 3 different (types) of objects for every... well, object, that the application works with. Having this separation, again, lends itself to the independence of the different layers. Hence, in order to fulfill every layer contract we have the Mappers file with appropriate extension functions. This way the DTOs are known only to the Api, the Domain models are used throughout and the Entities are used to perform CRUD actions.

**UI** - for the UI layer I decided to use Compose, as I will be the first to say it, I need the practice. I believe I can still do some more optimizations for the big Sports book list, but it works as intended for now. In any case, I did my best to manage state as high in the chain as I could, leaving the smaller componens stateless (like EventGrid item), which does help with optimization, not to mention sanity.

**Modularity** - The project is separated into modules with clear and precise roles. This ultimately helps wwith build times, developer experience and promotes the actual separation of concerns. Keeping modules as small as possible with as little responsibilities as possible has huge benefits in my experience, even if we're talking about developer QOL. Usually I seperate my projects even more, but for this project I decided that I didn't need any more and that this is clean enough, without going overboard.

**Module list**
```
app/                  # Main application module (themes, colors, entry point)
datasource/local/     # Local data sources (Room DB, entities)
datasource/remote/    # Remote data sources (API, DTOs)
feature/sports-book-list/ # Sports event list feature (ViewModel, UI, filtering)
gradle/               # Gradle configuration and version catalogs
```

## Tech Stack
- **Kotlin**
- **Jetpack Compose**
- **Coroutines & Flow**
- **Dagger** (DI)
- **Room** (local DB)
- **JUnit & Mockito** (unit tests)
- **Compose UI Test** (instrumented tests)

## Setup & Build
1. Clone the repository:
   ```sh
   git clone <repo-url>
   ```
2. Open in Android Studio.
3. Sync Gradle and build the project.
4. Run the app on an emulator or device running Android API 21+.

## Testing
- **Unit tests:**
  ```sh
  ./gradlew test
  ```
- **Instrumented UI tests:**
  ```sh
  ./gradlew connectedAndroidTest
  ```
  Or run directly from Android studio

## Timezone Handling
Event countdown timers and times are shown in the user's local timezone, with all event timestamps converted from UTC seconds to local time. (I assumed it's a UNIX timestamp in seconds)

