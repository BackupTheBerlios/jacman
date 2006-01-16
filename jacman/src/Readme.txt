Jacman - v0.3 - README
======================

Andrew Roberts (dev [at] andy-roberts [dot] net)
16th January 2006

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

What's New in 0.3?
==================

Nothing too major. It's mainly tidying up a lot of loose-ends and making the code
more robust. Here's a quick summary:

* Jon-Anders (SoniX) has been very busy. He did a lot of work in improving the 
  console that displays the pacman output. Is now much smoother and looks even
  better with its colour support.
* Jon-Anders also implemented experimental system tray support. Implementation is
  good on our end. Unfortunately, it turns out that the library (which is a wrapper
  to a native library) is not the most stable thing at the moment. Still, do play
  around to see what you think.
* A proper preferences dialog has been implemented. There's not a great deal of 
  properties that can be set at the moment. Still, it's better than editing the
  jacman.properties file directly. (Note this saves user settings in ~/.jacman)
* Changed default settings so that Jacman doesn't automatically shutdown after a 
  successful package operation (e.g., install). That seemed to cause a lot of bad
  press. It can be configured in the preferences dialog if you preferred the old
  behaviour.
* A simple language selection facility has been added. It does require a restart.
  This will be much improved in future releases to (hopefully) provide live
  language switching.
* Improved and tweaked lots of other little bits-and-bobs under the hood.

Installation
============

Their are two ways to install Jacman. 

1. Download the prebuilt Pacman package and install as normal:
  - Download the Pacman package from 
    http://download.berlios.de/jacman/jacman-0.3-1.pkg.tar.gz
  - `pacman -U /path/to/jacman-0.3-1.pkg.tar.gz'

  Or I understand you can specify URLs directly with pacman, e.g.:
  `  pacman -U http://download.berlios.de/jacman/jacman-0.3-1.pkg.tar.gz'

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

Alternatively, the source for v0.3 is available at:
http://download.berlios.de/jacman/jacman-0.3_src.jar

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