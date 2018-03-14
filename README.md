# ANova Checkout System

ANova Hacks laptop management and check-in system built with Ruby on Rails 5. Based on Inventorious.

!["Dashboard"](https://github.com/zmitzie/inventorious/blob/master/dashboard_screenshot.png "Dashboard")

## Description
Users (who have access to the app), can specify items, students (who can borrow items), and create borrow orders for students. Orders can then be marked as returned or canceled, or renewed for 7 days from a user. Users can also check in students and mentors to rooms and workshops. Only authenticated users can access the app and make changes. User registration is disabled.

## Usage

### Adding Students, Mentors, Items, and Rooms
There are two ways to add students, mentors, items, and rooms (workshops): individually and in bulk with a CSV. If you are uploading a CSV file, it has to follow the format specified here (including capitalization):

#### Students CSV Columns
* `name`: student name
* `email`: student email
* `upc`: student barcode number

#### Mentors CSV Columns
* `name`: mentor name
* `email`: mentor email
* `upc`: mentor barcode number

#### Items CSV Columns
* `name`: item name
* `category`: either "Laptop" or "Other"
* `quantity`: usually 1
* `remaining_quantity`: should be equal to quantity
* `description`: item description
* `upc`: item barcode number

#### Rooms/Workshops CSV Columns
Hackathon Check-in and check-out will also count as a room. For these, set the name to be "Hackathon Check-In" and "Hackathon Check-Out", respectively.
* `name`: workshop name
* `date`: MM/DD/YYYY format
* `time`: hh:mm AM/PM format
* `location`: optional field

### Barcode Assignment and Generation

#### Assigning Barcodes
We use EAN13 barcodes for our items, students, mentors, and workshops. The breakdown of the 12 digits in the barcode is as follows:
* First 6 digits: `978020` for all codes
* Next 2 digits:
  * `11` for students
  * `12` for mentors
  * `13` for items
* Last 4 digits: unique random code for each item of that type

### Checking Out Laptops
There are two ways to check out laptops to students: by filling out the form and by scanning their barcodes. To check out by barcode, navigate to the Scan Barcode page on the main sidebar and scan the laptop. This should take you to the item checkout page, where you can scan the student's barcode.

### Hackathon and Workshop Check-In
To check in/out students and mentors from the hackathon and workshops, navigate to the check in page on the sidebar. From there, click on the workshop name or icon to its left, and proceed to scan the student/mentor's barcode. The page should refresh with an updated room roster upon successful scanning. If it redirects to the homepage, the scan was unsuccessful.

## Installation Instructions

### Install Rails

* Make sure you have a Ruby version > 2.2.2 installed in your system
* Install [RubyGems](https://rubygems.org/pages/download)
* run ```gem install rails -v 5.0.2```

### Download Repo

* Download this repo, and unzip it
* ``` cd checkout-system``` to cd into the folder
* ``` bundle ```
* ``` rails db:migrate db:seed ```

### Run the server
* Run ```rails s``` to start the server
* Without stoping the server, open another terminal window and run ```rake jobs:work``` (needed in order for ActionMailer to work and deliver emails)
* Visit http://localhost:3000
* Use ```demo@demo.com```  ```change_me``` for the email and password. Change the password and email once you login. Create more users via the Console.
