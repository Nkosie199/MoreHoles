# More Holes

Molly the mole has one power: digging. Survive as many days as you can, made with [libGDX](https://libgdx.com/). It
runs in a browser and on a desktop.

**Play it:** https://nkosie199.github.io/MoreHoles/ (rebuilt and published by GitHub on every push to `master`, see
`.github/workflows/web.yml`).

## How it plays

- **Air.** Tunnels joined to a hole in the surface have air; the rest go stale (drawn darker). Without air Molly's
  air bar empties and then she suffocates.
- **Snakes** slither in through open holes and hunt Molly through the tunnels. They can't dig, so packing dirt into a
  tunnel walls them out.
- **Humans** walk across the surface and fill in every hole they pass.
- **Food.** Grubs turn up on the surface and feed and heal Molly, but out there **birds** dive at her.
- **Days.** Each day lasts 45 seconds. Like Tetris levels, every new day sends more and faster snakes, humans and
  birds, makes Molly hungrier, and is worth more points.

Holes are the whole game: Molly needs them to breathe and eat, and every hole is a way in for snakes.

**Controls:** arrows or WASD move, and moving into soil digs. Space packs dirt into the tunnel behind Molly (she earns
one dirt per tile dug, up to five). P or Esc pauses. On a phone, hold where Molly should go, tap Molly to pack dirt,
and tap the top bar to pause.

**Put it on LuckyB:** Profile → Publish a Game → **New Tab**, and use the link above as the Launch URL.

## Run it

- Desktop: `gradlew lwjgl3:run` (Windows: `gradlew.bat lwjgl3:run`).
- Browser: `gradlew html:dist` builds it into `html/build/dist`; serve that folder with any web server.
- Tests: `gradlew core:test` checks the game rules in `core/src/main/java/com/mygdx/moreholes/world`.

## Code

- `core/.../world`: the rules, with no drawing (`World` ties together `Burrow`, `Molly`, `Snake`, `Human`, `Bird`,
  `Food`, `DayClock` and `Difficulty`).
- `core/.../view`: draws a `World`.
- `core/...`: the screens and controls.
- `lwjgl3`: desktop launcher. `html`: browser launcher (GWT).
