# AppieCal

An automated system that allows you to seamlessly connect and sync
your `@AH account` schedule to a calendar like Apple Calendar.

The goal of this project was to learn a more about the **Caldav** protocol
and the way you communicate with it. And to learn the Kotlin language.

Using a guide from [Aurinko](https://www.aurinko.io/blog/caldav-apple-calendar-integration/) I was able to explore the capabilities of Apple's caldav server.
You can however use any server you like, as long as it supports **Basic Authentication**.

## Disclaimer

This project is created for educational purposes, however it can be used to
sync your own account. This project will only work if you work at **Albert Heijn** and have a pnl that works with the @AH app.

The information saved on drive is not encrypted either, keep this in mind when running it.

## Installation (simple)

Download and install the docker instance, make sure you have a folder called `data` linked to `/appiecal/data` and have given full permissions to the instance itself (this way it can read and write files in this folder).

1. Start the server and visit the `/authenticate`
2. Login with your pnl (it will ask for an email if you have not logged in with that before, they will all look like this: `pnlxxx@emea.royalahold.net`)
3. You will be sent to a dead url, copy the values after "code" and visit `/authorize?code=CODE HERE`.
4. After that go to the `caldav.json` file and fill in the credentials (if you use Apple Calendar [read this guide](https://www.reddit.com/r/Thunderbird/comments/1dpop9d/icloud_calendar_sync_without_addon_get_icloud/) about how to obtain the required data).
5. Restart your server and visit `/calendar/sync` to sync (everything should work automatically from here)