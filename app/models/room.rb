class Room < ApplicationRecord
	validates :name, presence: true
	validates :date, presence: true
	validates :time, presence: true

	require 'csv'

	def self.import(file)
	  CSV.foreach(file.path, headers:true) do |row|
	  	Room.create! row.to_hash
	  end
	end
end
