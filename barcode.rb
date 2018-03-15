require 'barby'
require 'barby/barcode/ean_13'
require 'barby/outputter/png_outputter'

# 12 digits: 6 company, 2 type, 4 id?
# types: items, members, rooms

# barcode = Barby::EAN13.new('123456119999')
# barcode = Barby::EAN13.new('978020118010')

require 'csv'

csv_text = File.read('tmp/laptops.csv')
csv = CSV.parse(csv_text, :headers => true, :encoding => 'ISO-8859-1')
csv.each do |row|
  puts row['Name']
  puts row['Barcode']
  barcode = Barby::EAN13.new(row['Barcode'])
  fname = 'tmp/barcodes/laptops/' + row['Name'] + '.png'
  File.open(fname, 'wb') do |f|
	f.write barcode.to_png
  end
end

