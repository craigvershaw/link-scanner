# Link Scanner
Link Scanner is a Liferay portlet application for testing the status of links and images within the portal's different content types. This will assist with finding and fixing broken links in your content. Links Scanner currently supports blogs, bookmarks, calendar, message boards, RSS portlet, web and wiki (HTML format only) content types.

## Instructions
After Link Scanner has been installed, you will find it located in the Control Panel or Manage Content areas at the bottom of the Content section.

![MainScreenShot](https://raw.githubusercontent.com/craigvershaw/link-scanner/master/docroot/images/screenshots/link-scanner-screen01-main.jpg)

1. Select the content type that you would like to scan.
1. Choose whether to scan for links, images or both.
1. You also have the option to use the current web browser's user agent when testing links. Disabling this option is not recommended as it makes link scanner less reliable.

Click Scan Links to begin. Link Scanner will look for links and/or images within the current Liferay site for the chosen content type.

![WebScreenShot](https://raw.githubusercontent.com/craigvershaw/link-scanner/master/docroot/images/screenshots/link-scanner-screen07-web.jpg)

Each content item will be linked to the editor for that content set to open in a new window. If none of the chosen content items contain links or images, no results will be displayed. Progress will be displayed as Link Scanner tests each link.

![BlogsScreenShot](https://raw.githubusercontent.com/craigvershaw/link-scanner/master/docroot/images/screenshots/link-scanner-screen02-blogs.jpg)

The left column displays the status for each link:

1. Empty box is for links that have not been tested yet.
1. Green is shown when the links works properly.
1. Orange is shown for links that work, but return a redirect code indicating that the location may have been moved.
1. Red is shown for broken links.

Hover your mouse over the status to see more details.

* Links to external sites will show "WS" along with the HTTP response code. These links are tested via a web service provided by Link Scanner. This means they are tested from the Liferay application server.
* Links to portal links will show "AJAX" and whether or not the test was successful. The test uses an AJAX request to verify that the link can be accessed. This uses the currently logged-in user's Liferay session and permissions, so links to restricted content will work if the current user has permission to access them.

## Configuration

Use the gear icon to access the Configuration screen. Here you may add additional prefixes for links that exist within your portal. These may include domains that are enabled through [Cross-Origin Resource Sharing (CORS)](http://en.wikipedia.org/wiki/Cross-Origin_Resource_Sharing). Links that begin with these prefixes will be tested via AJAX within the current user's browser.

![ConfigurationScreenShot](https://raw.githubusercontent.com/craigvershaw/link-scanner/master/docroot/images/screenshots/link-scanner-screen09-configuration.jpg)

## Advanced

Testing your links is dependent on a wide range of factors. Please keep these in mind when evaluating your link scans:

* Network location / speed
* Firewall rules
* Permissions

## Credits

Some images and icons originate from [FatCow](http://www.fatcow.com/free-icons) and [Liferay](https://www.liferay.com/)