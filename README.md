# PGRF1 – Task 1: Line and polygon rasterization

Semester project for the **PGRF1 (Computer Graphics)** course at the Faculty of Informatics and Management, University of Hradec Králové.

**Author:** Radim Krb

## Requirements

* Java 21 or newer
* IntelliJ IDEA or another Java IDE

## Running the project

Open the project in your IDE and run the `Main` class.

## Controls

| Input | Action |
|---|---|
| `L` | Line mode – draw separate lines |
| `P` | Polygon mode – add vertices to the polygon |
| Left mouse button (drag) | Line mode: press = start point, release = end point<br>Polygon mode: add a new vertex into the nearest edge |
| Right mouse button (drag) | Move the nearest vertex (polygon vertex or line endpoint) |
| `Shift` + right click | Delete the nearest vertex (for a line, the whole line is deleted) |
| `C` | Clear the canvas and all data |

The current mode and the controls are shown in the window title. While dragging, a red rubber-band preview is drawn.

## Features

* **Line rasterization** – trivial algorithm (y = kx + q), working in all directions; steep lines step along the y axis, vertical and degenerate (single point) lines are handled, drawing outside the canvas is clipped.
* **Lines** – interactive drawing with a rubber-band preview.
* **Polygon** – stored as a list of vertices, edges are derived, the polygon is always closed.
* **Adding vertices** – a new vertex is inserted into the nearest edge (point-to-segment distance); the preview shows the target edge.
* **Editing** – moving and deleting vertices of the polygon and of lines.

## Project structure

* `Main` – application entry point
* `view` – `Window` (main application window) and `Canvas` (raster drawing surface)
* `controller` – `Controller` handles user input, modes and rendering
* `graphics` – `Raster` / `RasterImage`, line rasterizer (`LineRasterizer`, `TrivialLineRasterizer`) and polygon rasterizer (`PolygonRasterizer`, `PolygonOutlineRasterizer`)
* `model` – `Point`, `Line`, `Polygon`
