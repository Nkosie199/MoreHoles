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

## Controls

| | Keyboard (desktop and browser) | Touch (phones and tablets) |
|---|---|---|
| Move, and dig by moving into soil | Arrows or WASD | Hold your finger where Molly should go |
| Pack dirt into the tunnel behind Molly | Space | Tap Molly |
| Pause / carry on | P or Esc | Tap the top bar |
| Start, or play again after game over | Enter or Space | Tap anywhere |

Molly earns one dirt per tile dug and can carry five.

## Platforms

- **Browser** (what LuckyB players get): fills the window and keeps its square shape, so it works in portrait and
  landscape. On a desktop browser, click the game once so it gets the keyboard. On a phone use the touch controls.
- **Desktop** (Windows, macOS, Linux): a 640x640 window, keyboard only.

## Build and run

- Desktop: `gradlew lwjgl3:run` (Windows: `gradlew.bat lwjgl3:run`).
- Browser: `gradlew html:dist` builds into `html/build/dist`; serve it, e.g.
  `python -m http.server 8090 --directory html/build/dist`, then open http://localhost:8090.
- Tests: `gradlew core:test` checks the game rules in `core/src/main/java/com/mygdx/moreholes/world`.

## Deploy

Every push to `master` runs `.github/workflows/web.yml`, which builds the browser version and pushes it to the
`gh-pages` branch. GitHub Pages serves it at https://nkosie199.github.io/MoreHoles/ once Pages is turned on in the
repo's Settings → Pages (deploy from branch `gh-pages`, `/ (root)`).

**Put it on LuckyB:** Profile → Publish a Game, Launch Mode **New Tab**, the link above as Launch URL, 1 COA. The full
process is in LuckyB's `docs/HOST_YOUR_GAME.md`.

## Code

- `core/.../world`: the rules, with no drawing (`World` ties together `Burrow`, `Molly`, `Snake`, `Human`, `Bird`,
  `Food`, `DayClock` and `Difficulty`).
- `core/.../view`: draws a `World`.
- `core/...`: the screens and controls.
- `lwjgl3`: desktop launcher. `html`: browser launcher (GWT).
