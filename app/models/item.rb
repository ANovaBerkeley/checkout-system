class Item < ApplicationRecord
  has_many :orders
  has_many :students, through: :orders

  validates :name, presence: true
  validates :category, presence: true
  validates :upc, presence: true
  # if importing a csv, also include quantity

  require 'csv'

  def self.import(file)
  	CSV.foreach(file.path, headers:true) do |row|
  		Item.create! row.to_hash
  	end
  end
end
