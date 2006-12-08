Jacman - v0.3 - README
======================

Andrew Roberts (dev [at] andy-roberts [dot] net)
8th December 2006

Overview
========

Jacman is a GUI frontend to the excellent pacman software management software 
that comes with the equally excellent ArchLinux. Hopefully the "J" makes it 
obvious that this is a Java application. Arch's philosophy is to keep things 
simple and to keep the user in control. Generally, GUI based configuration 
tools were deemed "anti-Arch" as they tended to contradict the philosophy by 
concealing functionality. Whilst developing Jacman, it has been a design goal 
to not break the philosophy. Whilst Jacman is only a front-end to pacman, it 
doesn't provide access to all of pacman's functionality, so there is still no 
replacement for the real thing :)

However, there was a reason for that! I wanted Jacman to provide a visual 
window for only a subset of common tasks: installing, removing, updating and 
rolling-back. Some people may like this, as it allows people to quickly explore 
which packages are available.

Features
========

Jacman provides an interface to the most common functionality of Pacman: 

 * installing new packages.
 * updating installed packages.
 * removing unwanted packages.
 * rolling back packages to a previously installed version.

What's New in 0.4?
==================

Nothing too major - largely cosmetic changes. I like to ensure Jacman stays well
ahead in the eye candy department. It's not all bling, however. Here's a quick summary:
 * New look and feel to the whole application. I think the theme is a lot lighter and
   fresher. Look out for nice roll-over effects in tables, lists, buttons (especially
   with icons), etc, etc. 
 * More filtering options for install dialog.
 * Improved search/filtering performance.
 * Install and Update dialogs have coloured columns indicate whether packages are up-
   to-date or now (a la distrowatch).
 * Improved and tweaked a few of other little bits-and-bobs under the hood.

Installation
============

Their are two ways to install Jacman. 

1. Download the prebuilt Pacman package and install as normal:
  - Download the Pacman package from 
    http://download.berlios.de/jacman/jacman-0.4.pkg.tar.gz
  - `pacman -U /path/to/jacman-0.4.pkg.tar.gz'

  Or I understand you can specify URLs directly with pacman, e.g.:
  `  pacman -U http://download.berlios.de/jacman/jacman-0.4.pkg.tar.gz'

2. Jacman is is the Community repository. To access this with pacman, edit 
   /etc/pacman.conf and the instructions are in the file itself.
   - `pacman -Sy jacman' will sync your pacman databases and install jacman.

3. Download the PKGBUILD from the AUR repository (http://aur.archlinux.org/) 
   and install as per AUR instructions.

Usage
=====

Once installed, you can run Jacman as follows:

  jacman [-c other_pacman_conf]

By default, Jacman will assume that Pacman's config file is located at 
/etc/pacman.conf. If you wish to specify an alternative, use the -c flag and 
specify the path to an alternative.

Known issues
============

* Much effort has gone in to making the console cope with the curses-based output
  that Pacman generates. It *should* be fine, but should Pacman change its output
  format in a new release, then the console presentation *could* be broken. But
  don't worry, we'll keep on top of it!

* Jacman obviously relies on a correctly formed pacman.conf. It's unlikely 
  that an ArchLinux could survive without one, and so I've not implemented any 
  methods to check the conf file's correctness. What Jacman does with a malformed 
  pacman.conf is unknown (not much I expect!) and may not be very graceful.

* Jacman doesn't prompt you to sign in as root like many GUI apps do. If you 
  try and do something that you don't have permission for, expect an error 
  telling you so!

* Jacman relies on Tahoma fonts for the text in the main menus. This is perhaps
  not the best dependency to have, and so will investigate more apt OSS fonts
  for future releases.

* Logo in the main window doesn't honour the transparency. Don't know why! I think
  it's a Java bug.

Contribute
==========

The easiest way to contribute is to simply use Jacman. I'd strongly encourage 
you to share any bugs and/or feature requests.

For anyone who's interested, the Jacman project has a CVS server courtesy of
Berlios. Anyone interested in checking out the current development branch should
head over to http://developer.berlios.de/projects/jacman/ for further details.
If you want to join the Jacman development team, please contact me.

Alternatively, the source for v0.4 is available at:
http://download.berlios.de/jacman/jacman-0.4_src.jar

Jacman has i18n support throughout and so I'm looking for translators. The 
English labels file is called JacmanLabels.properties. If you translate this 
file, do not hesitate to get in touch and I'll happily bundle the translation 
file with future releases.

Contact
=======

If you wish to contact the developer about Jacman to suggest future
features, bugs or anything that you want, please email me at:

dev [at] andy-roberts [dot] net

* Anti-spam format. Please remove all spaces, and replace '[at]' with
  the '@' symbol (no quotes), etc.
  
Copyright 2005-2006, The Jacman Team.