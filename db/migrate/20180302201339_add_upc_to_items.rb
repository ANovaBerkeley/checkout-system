class AddUpcToItems < ActiveRecord::Migration[5.0]
  def change
    add_column :items, :upc, :string
  end
end
